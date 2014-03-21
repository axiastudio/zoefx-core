package com.axiastudio.zoefx.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 13:00
 */
public class Model<T> {

    private T entity;
    private Map<String, ItemStringProperty> properties = new HashMap();

    public Model(T entity) {
        this.entity = entity;
        initialize();
    }

    private void initialize(){
        for( Method method: entity.getClass().getMethods() ){
            if( method.getName().startsWith("get") ){
                String name = method.getName().substring(3);
                Class<?> returnType = method.getReturnType();
                if( returnType == String.class ){
                    ItemStringProperty item = new ItemStringProperty(entity, name);
                    properties.put(name, item);
                }
            }
        }
        for( Field field: entity.getClass().getFields() ){
            String name = field.getName();
            if( field.getType() == String.class ){
                ItemStringProperty item = new ItemStringProperty(entity, name);
                properties.put(name, item);
            }
        }
    }

    public ItemStringProperty getProperty(String name){
        return properties.get(name);
    }

}
