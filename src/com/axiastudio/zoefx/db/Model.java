package com.axiastudio.zoefx.db;

import com.axiastudio.zoefx.beans.property.*;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
    private Map<String, Callback> callbacks = new HashMap();

    public Model(E entity) {
        this.entity = entity;
    }

    public Property getProperty(String name){
        if( properties.containsKey(name) ){
            return properties.get(name);
        }
        Property property = ItemPropertyBuilder.create().bean(entity).property(name).build();
        properties.put(name, property);
        return property;
    }

    public Callback getCallback(String name, String columnId) {
        String key = name+"."+columnId;
        if( callbacks.containsKey(key) ){
            return callbacks.get(key);
        }
        Callback callback = createCallback(name, columnId);
        return callback;
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
                ObservableValue<T> property = ItemPropertyBuilder.create().bean(bean).property(idColumn).build();
                return property;
            }
        };
        return callback;
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
