package com.axiastudio.zoefx.beans.property;

import com.axiastudio.zoefx.beans.AccessType;
import com.axiastudio.zoefx.beans.BeanAccess;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * User: tiziano
 * Date: 10/04/14
 * Time: 12:00
 */
public class CallbackBuilder {

    private Object bean;
    private String collectionName;
    private String propertyName;


    public CallbackBuilder() {
    }

    public static CallbackBuilder create(){
        return new CallbackBuilder();
    }

    public CallbackBuilder bean(Object bean){
        this.bean = bean;
        return  this;
    }

    public CallbackBuilder property(String name){
        String[] split = name.split("\\.");
        collectionName = split[0];
        propertyName = split[1];
        return this;
    }

    public Callback build(){
        BeanAccess beanAccess = new BeanAccess(bean, collectionName);
        Class pClass=null; // generic class of the collection
        Class tClass=null; // class of the property
        if( beanAccess.getAccessType().equals(AccessType.FIELD) ){
            Field field = null;
            try {
                field = bean.getClass().getField(collectionName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Type type = field.getGenericType();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            pClass = (Class) actualTypeArguments[0];
        } else if(beanAccess.getAccessType().equals(AccessType.METHOD) ){
            try {
                Method method = bean.getClass().getMethod("get" + collectionName.substring(0, 1).toUpperCase() + collectionName.substring(1));
                ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
                Type[] actualTypeArguments = pt.getActualTypeArguments();
                pClass = (Class) actualTypeArguments[0];
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        tClass = String.class; // XXX: wrong
        return createCallback(pClass, tClass, propertyName);
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

}
