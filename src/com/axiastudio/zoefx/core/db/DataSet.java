package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.beans.BeanAccess;
import com.axiastudio.zoefx.core.view.Model;
import javafx.beans.property.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 25/03/14
 * Time: 16:52
 */
public class DataSet<E> {

    private List<E> store;
    private Integer currentIndex;
    private Model<E> currentModel=null;
    private Map<Property, Object> olds = new HashMap();
    private Boolean dirty=Boolean.FALSE;

    public DataSet(List<E> store) {
        this.store = store;
        goFirst();
    }

    public void setStore(List<E> store) {
        this.store = store;
        goFirst();
        olds.clear();
        dirty = Boolean.FALSE;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Model<E> newModel() {
        E entity = store.get(currentIndex);
        currentModel = new Model(entity);
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
        dirty = Boolean.TRUE;
    }

    public void putOldValue(Property property, Object oldValue){
        if( !olds.keySet().contains(property) ){
            olds.put(property, oldValue);
        }
    }

    public void revert() {
        for( Property property: olds.keySet() ){
            property.setValue(olds.get(property));
        }
        olds.clear();
        dirty = Boolean.FALSE;
    }

    public void commit() {
        Database db = Utilities.queryUtility(Database.class);
        if( db != null ) {
            Manager<E> manager = db.createManager(getEntityClass());
            E entity = store.get(currentIndex);
            manager.commit(entity);
        }
        olds.clear();
        dirty = Boolean.FALSE;
    }

    private Class<E> getEntityClass() {
        return (Class<E>) store.get(0).getClass();
    }

    public void create() {
        Database db = Utilities.queryUtility(Database.class);
        if( db != null ) {
            Manager<E> manager = db.createManager(getEntityClass());
            E entity = manager.create();
            store.add(entity);
        } else {
            try {
                E entity = getEntityClass().newInstance();
                store.add(entity);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        goLast();
    }

    public void create(String name) {
        Database db = Utilities.queryUtility(Database.class);
        E entity = store.get(currentIndex);
        BeanAccess<Collection> beanAccess = new BeanAccess<Collection>(entity, name);
        Collection collection = beanAccess.getValue();
        Class<?> genericReturnType = beanAccess.getGenericReturnType();
        if( db != null ) {
            Manager<?> manager = db.createManager(genericReturnType);
            Object o = manager.create();
            collection.add(o);
        } else {
            try {
                Object o = genericReturnType.newInstance();
                collection.add(o);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete() {

    }
}
