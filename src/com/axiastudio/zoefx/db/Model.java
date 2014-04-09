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
    private Map<String, PropertyValueFactory> propertyValueFactories = new HashMap();

    public Model(E entity) {
        this.entity = entity;
        initialize();
    }

    private void initialize(){
        // methods
        for( Method method: entity.getClass().getMethods() ){
            if( method.getName().startsWith("get") ){
                String name = method.getName().substring(3).toLowerCase();
                Class<?> type = method.getReturnType();
                Property item = createProperty(name, type);
                if( item != null ){
                    properties.put(name, item);
                }
                if( Collection.class.isAssignableFrom(type) ){
                    // TODO: createPropertyValueFactory ?
                }
            }
        }
        // fields
        for( Field field: entity.getClass().getFields() ){
            String name = field.getName();
            Class<?> type = field.getType();
            Property item = createProperty(name, type);
            if( item != null ){
                properties.put(name, item);
            }
            if( Collection.class.isAssignableFrom(type) ){
                ParameterizedType pt = (ParameterizedType) field.getGenericType();
                Type[] actualTypeArguments = pt.getActualTypeArguments();
                Class collectionClass = (Class) actualTypeArguments[0];
                for( Field collectionClassField: collectionClass.getFields() ) {
                    String columnId = collectionClassField.getName();
                    PropertyValueFactory propertyValueFactory = createPropertyValueFactory(name, columnId);
                    if (propertyValueFactory != null) {
                        propertyValueFactories.put(name + "." + columnId, propertyValueFactory);
                    }
                }
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
        return propertyValueFactories.get(name+"."+idColumn);
    }

    private PropertyValueFactory createPropertyValueFactory(String name, String idColumn) {
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
        PropertyValueFactory<T, S> propertyValueFactory = new PropertyValueFactory<T, S>(name);
        return propertyValueFactory;
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
