package com.axiastudio.zoefx.core.view;

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
    private Map<String, ZoeFXProperty> propertiesCache = new HashMap();
    private Map<String, Callback> callbacksCache = new HashMap();

    public Model(E entity) {
        this.entity = entity;
    }

    public ZoeFXProperty getProperty(String name){
        if( propertiesCache.containsKey(name) ){
            return propertiesCache.get(name);
        }
        return null;
    }

    public ZoeFXProperty getProperty(String name, Class<?> klass){
        if( propertiesCache.containsKey(name) ){
            return propertiesCache.get(name);
        }
        ZoeFXProperty property = ItemPropertyBuilder.create(klass).bean(entity).field(name).build();
        propertiesCache.put(name, property);
        return property;
    }

    public Callback getCallback(String name, String columnId) {
        return getCallback(name, columnId, null);
    }

    public Callback getCallback(String name, String columnId, String lookup) {
        String key = name+"."+columnId;
        if( callbacksCache.containsKey(key) ){
            return callbacksCache.get(key);
        }
        CallbackBuilder cb = CallbackBuilder.create().beanClass(entity.getClass()).field(key);
        if( lookup != null ){
            cb = cb.lookup(lookup);
        }
        Callback callback = cb.build();
        return callback;
    }

    public Class<? extends Object> getEntityClass() {
        return entity.getClass();
    }

    public E getEntity() {
        return entity;
    }

    @Override
    protected void finalize() throws Throwable {
        for( Property property: propertiesCache.values() ){
            property.unbind();
        }
    }

}
