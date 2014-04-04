package com.axiastudio.zoefx.db;

import javafx.beans.property.Property;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 13:00
 */
public class Model<T> {

    private T entity;
    private Map<String, Property> properties = new HashMap();

    public Model(T entity) {
        this.entity = entity;
        initialize();
    }

    private void initialize(){
        // methods
        for( Method method: entity.getClass().getMethods() ){
            if( method.getName().startsWith("get") ){
                String name = method.getName().substring(3).toLowerCase();
                Property item = createProperty(name, method.getReturnType());
                if( item != null ){
                    properties.put(name, item);
                }
            }
        }
        // fields
        for( Field field: entity.getClass().getFields() ){
            String name = field.getName();
            Property item = createProperty(name, field.getType());
            if( item != null ){
                properties.put(name, item);
            }
        }
    }

    private Property createProperty(String name, Class<?> returnType){
        Property item=null;
        if( returnType == String.class ){
            item = new ItemStringProperty(entity, name);
        } else if( returnType == Boolean.class ){
            item = new ItemBooleanProperty(entity, name);
        } else if( returnType == Date.class ){
            item = new ItemDateProperty(entity, name);
        } else if( returnType.isEnum() ){
            item = new ItemEnumProperty(entity, name);
        }
        return item;
    }

    public Property getProperty(String name){
        return properties.get(name);
    }

}
