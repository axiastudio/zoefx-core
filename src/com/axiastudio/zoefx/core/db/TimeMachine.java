package com.axiastudio.zoefx.core.db;

import javafx.beans.property.Property;

import java.util.*;

/**
 * User: tiziano
 * Date: 26/05/14
 * Time: 20:52
 */
public class TimeMachine {

    private List<Map<Property, Object>> snapshots = new ArrayList<>();

    public void resetAndCreateSnapshot(Collection<Property> properties){
        reset();
        createSnapshot(properties);
    }

    public void createSnapshot(Collection<Property> properties){
        Map<Property, Object> snapshot = new HashMap<>();
        for( Property property: properties ){
            snapshot.put(property, property.getValue());
        }
        snapshots.add(snapshot);
    }

    public void undo(){
        int last = snapshots.size() - 1;
        Map<Property, Object> snapshot = snapshots.get(last);
        snapshots.remove(last);
        for(Property property: snapshot.keySet() ){
            property.setValue(snapshot.get(property));
        }
    }

    public void rollback(){
        Map<Property, Object> snapshot = snapshots.get(0);
        for(Property property: snapshot.keySet() ){
            property.setValue(snapshot.get(property));
        }
        snapshots.clear();
    }

    public void reset(){
        snapshots.clear();
    }

}
