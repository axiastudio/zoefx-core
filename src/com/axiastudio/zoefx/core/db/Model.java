package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.beans.property.*;
import javafx.beans.property.Property;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 13:00
 */
public class Model<E> {

    private E entity;
    private Map<String, Property> propertiesCache = new HashMap();
    private Map<String, Callback> callbacksCache = new HashMap();

    public Model(E entity) {
        this.entity = entity;
    }

    public Property getProperty(String name){
        if( propertiesCache.containsKey(name) ){
            return propertiesCache.get(name);
        }
        Property property = ItemPropertyBuilder.create().bean(entity).property(name).build();
        propertiesCache.put(name, property);
        return property;
    }

    public Callback getCallback(String name, String columnId) {
        String key = name+"."+columnId;
        if( callbacksCache.containsKey(key) ){
            return callbacksCache.get(key);
        }
        Callback callback = CallbackBuilder.create().bean(entity).property(key).build();
        return callback;
    }

}
