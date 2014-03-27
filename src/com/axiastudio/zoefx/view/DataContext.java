package com.axiastudio.zoefx.view;

import com.axiastudio.zoefx.db.Model;
import javafx.beans.property.Property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 25/03/14
 * Time: 16:52
 */
public class DataContext<T> {

    private List<T> store;
    private Integer currentIndex;
    private Map<Property, Object> changes = new HashMap();
    private Boolean dirty=Boolean.FALSE;

    public DataContext(List<T> store) {
        this.store = store;
        goFirst();

    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Model<T> getModel(){
        T entity = store.get(currentIndex);
        Model<T> model = new Model(entity);
        return model;
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
}
