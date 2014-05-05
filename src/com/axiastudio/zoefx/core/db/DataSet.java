package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.view.Model;
import javafx.beans.property.Property;

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
    private Map<Property, Object> changes = new HashMap();
    private Boolean dirty=Boolean.FALSE;

    public DataSet(List<E> store) {
        this.store = store;
        goFirst();
    }

    public void setStore(List<E> store) {
        this.store = store;
        goFirst();
        changes.clear();
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

    public void addChange(Property property, Object oldValue, Object newValue){
        if( !changes.keySet().contains(property) ){
            changes.put(property, oldValue);
            getDirty();
        }
    }

    public void revert() {
        for( Property property: changes.keySet() ){
            property.setValue(changes.get(property));
        }
        changes.clear();
        dirty = Boolean.FALSE;
    }

    public void commit() {
        Database db = Utilities.queryUtility(Database.class);
        if( db != null ) {
            Manager<E> manager = db.createManager(getEntityClass());
            E entity = store.get(currentIndex);
            manager.commit(entity);
        }
        changes.clear();
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

    public void delete() {

    }
}
