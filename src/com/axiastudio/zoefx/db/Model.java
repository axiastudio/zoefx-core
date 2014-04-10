package com.axiastudio.zoefx.db;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
                String name = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
                Class<?> type = method.getReturnType();
                Property item = createProperty(name, type);
                if( item != null ){
                    properties.put(name, item);
                }
                if( Collection.class.isAssignableFrom(type) ){
                    ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
                    createAndRegisterPropertyValueFactory(name, pt);
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
                createAndRegisterPropertyValueFactory(name, pt);
            }
        }
    }

    private void createAndRegisterPropertyValueFactory(String name, ParameterizedType pt) {
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        Class collectionClass = (Class) actualTypeArguments[0];
        for( Field field: collectionClass.getFields() ) {
            String columnId = field.getName();
            PropertyValueFactory propertyValueFactory = createPropertyValueFactory(name, columnId);
            if (propertyValueFactory != null) {
                propertyValueFactories.put(name + "." + columnId, propertyValueFactory);
            }
        }
        for( Method method: collectionClass.getMethods() ) {
            String columnId = method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
            PropertyValueFactory propertyValueFactory = createPropertyValueFactory(name, columnId);
            if (propertyValueFactory != null) {
                propertyValueFactories.put(name + "." + columnId, propertyValueFactory);
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
            try {
                Method method = entity.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                Class klass = classFromCollectionMethod(method);
                PropertyValueFactory propertyValueFactory = createPropertyValueFactory(klass, String.class, idColumn);
                return propertyValueFactory;
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private <S, T> PropertyValueFactory<S, T> createPropertyValueFactory(Class<S> sClass, Class<T> tClass, String name){
        PropertyValueFactory<S, T> propertyValueFactory = new PropertyValueFactory<S, T>(name);
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


    public Callback getCallback(String name, String columnId) {
        return createCallback(name, columnId);
    }

    private <P, R> Callback<TableColumn.CellDataFeatures<P, R>, ObservableValue<R>> createCallback(String name, String idColumn){
        try {
            Field field = entity.getClass().getField(name);
            Class klass = classFromCollectionField(field);
            Callback callback = createCallback(klass, String.class, idColumn);
            return callback;
        } catch (NoSuchFieldException e) {
            try {
                Method method = entity.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                Class klass = classFromCollectionMethod(method);
                Callback callback = createCallback(klass, String.class, idColumn);
                return callback;
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn){

        Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> callback = new Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>>() {

            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<P, T> prCellDataFeatures) {
                P bean = prCellDataFeatures.getValue();
                ObservableValue<T> property=null;
                if( tClass == String.class ) {
                    property = (ObservableValue<T>) new ItemStringProperty(bean, idColumn);
                } else if( tClass == Boolean.class ) {
                    property = (ObservableValue<T>) new ItemBooleanProperty(bean, idColumn);
                } // TODO: others...
                return property;
            }
        };
        return callback;
    }

}
