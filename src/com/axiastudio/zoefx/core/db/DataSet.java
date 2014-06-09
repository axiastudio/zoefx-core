package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.beans.BeanAccess;
import com.axiastudio.zoefx.core.events.DataSetEvent;
import com.axiastudio.zoefx.core.events.DataSetEventGenerator;
import com.axiastudio.zoefx.core.events.DataSetEventListener;
import com.axiastudio.zoefx.core.view.Model;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 25/03/14
 * Time: 16:52
 */
public class DataSet<E> implements DataSetEventGenerator {

    private List<E> store;
    private Integer currentIndex;
    private Model<E> currentModel=null;
    private Boolean dirty=Boolean.FALSE;
    private List<DataSetEventListener> dataSetEventListeners = new ArrayList<DataSetEventListener>();


    public DataSet(List<E> store) {
        this.store = store;
        goFirst();
    }

    public void setStore(List<E> store) {
        this.store = store;
        goFirst();
        fireDataSetEvent(new DataSetEvent(DataSetEvent.STORE_CHANGED));
        dirty = Boolean.FALSE;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Model<E> newModel() {
        E entity = store.get(currentIndex);
        currentModel = new Model(entity);
        fireDataSetEvent(new DataSetEvent(DataSetEvent.INDEX_CHANGED));
        return currentModel;
    }

    public Model<E> getCurrentModel() {
        return currentModel;
    }

    public void goFirst() {
        currentIndex = 0;
    }

    public void goLast() {
        currentIndex = store.size()-1;
    }

    public void goNext() {
        if( currentIndex < store.size()-1 ){
            currentIndex++;
        }
    }

    public void goPrevious() {
        if( currentIndex>0 ){
            currentIndex--;
        }
    }

    public Integer size() {
        return store.size();
    }

    public Boolean isDirty() {
        return dirty;
    }

    public void getDirty() {
        if( !dirty ) {
            dirty = Boolean.TRUE;
            fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
        }
    }

    public void revert() {
        dirty = Boolean.FALSE;
        fireDataSetEvent(new DataSetEvent(DataSetEvent.REVERTED));
    }

    public void commit() {
        Database db = Utilities.queryUtility(Database.class);
        if( db != null ) {
            Manager<E> manager = db.createManager(getEntityClass());
            E entity = store.get(currentIndex);
            manager.commit(entity);
        }
        dirty = Boolean.FALSE;
        fireDataSetEvent(new DataSetEvent(DataSetEvent.COMMITED));
    }

    private Class<E> getEntityClass() {
        return (Class<E>) store.get(0).getClass();
    }

    public E create() {
        Database db = Utilities.queryUtility(Database.class);
        if( db != null ) {
            Manager<E> manager = db.createManager(getEntityClass());
            E entity = manager.create();
            store.add(entity);
            currentIndex = store.size()-1;
            fireDataSetEvent(new DataSetEvent(DataSetEvent.CREATED));
            return entity;
        } else {
            try {
                E entity = getEntityClass().newInstance();
                store.add(entity);
                currentIndex = store.size()-1;
                fireDataSetEvent(new DataSetEvent(DataSetEvent.CREATED));
                return entity;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object create(String name) {
        Database db = Utilities.queryUtility(Database.class);
        E parentEntity = store.get(currentIndex);
        BeanAccess<Collection> beanAccess = new BeanAccess<>(parentEntity, name);
        Collection collection = beanAccess.getValue();
        Class<?> genericReturnType = beanAccess.getGenericReturnType();
        if( db != null ) {
            Manager<?> manager = db.createManager(genericReturnType);
            Object entity = manager.create();
            collection.add(entity);
            fireDataSetEvent(new DataSetEvent(DataSetEvent.ROWS_CREATED));
            fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
            return entity;
        } else {
            try {
                Object entity = genericReturnType.newInstance();
                collection.add(entity);
                fireDataSetEvent(new DataSetEvent(DataSetEvent.ROWS_CREATED));
                fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
                return entity;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delete() {
        //fireDataSetEvent(new DataSetEvent(DataSetEvent.DELETE));
    }

    /*
     *  EVENTS
     */
    @Override
    public void addDataSetEventListener(DataSetEventListener listener) {
        dataSetEventListeners.add(listener);
    }

    private void fireDataSetEvent(DataSetEvent event){
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "{0} event fired", event.getEventType().getName());
        for(DataSetEventListener listener: dataSetEventListeners) {
            listener.dataSetEventHandler(event);
        }
    }
}
