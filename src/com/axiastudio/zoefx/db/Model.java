package com.axiastudio.zoefx.db;

import javafx.beans.property.Property;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 13:00
 */
public class Model<E> {

    private E entity;
    private Map<String, Property> properties = new HashMap();

    public Model(E entity) {
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
        } else if( returnType == Collection.class ){
            item = new ItemListProperty(entity, name);
        }
        return item;
    }

    public Property getProperty(String name){
        return properties.get(name);
    }

    public PropertyValueFactory getPropertyValueFactory(String name, String idColumn) {
        try {
            Field field = entity.getClass().getField(name);
            Class klass = classFromCollectionField(field);
            PropertyValueFactory propertyValueFactory = createPropertyValueFactory(klass, String.class, idColumn);
            return propertyValueFactory;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T, S> PropertyValueFactory<T, S> createPropertyValueFactory(Class<T> klassE, Class<S> klassS, String name){
        try {
            Field field = klassE.getField(name);
            //Class klass = classFromCollectionField(field);
            PropertyValueFactory<T, S> propertyValueFactory = new PropertyValueFactory<T, S>(name);
            return propertyValueFactory;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class classFromCollectionField(Field field){
        Type type = field.getGenericType();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return (Class) actualTypeArguments[0];
    }

    public static Class classFromCollectionMethod(Method method){
        ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        return (Class) actualTypeArguments[0];
    }



}
