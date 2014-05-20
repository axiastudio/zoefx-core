package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanClassAccess;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * User: tiziano
 * Date: 10/04/14
 * Time: 12:00
 */
public class CallbackBuilder {

    private Object bean;
    private String collectionName;
    private String propertyName;
    private String lookup=null;


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

    public CallbackBuilder lookup(String lookup){
        this.lookup = lookup;
        return this;
    }

    public Callback build(){
        BeanClassAccess beanClassAccess = new BeanClassAccess(bean.getClass(), collectionName);
        Class<?> pClass = beanClassAccess.getGenericReturnType();
        BeanClassAccess beanPropertyClassAccess = new BeanClassAccess(pClass, propertyName);
        Class<?> tClass = beanPropertyClassAccess.getReturnType();
        return createCallback(pClass, tClass, propertyName, lookup);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn){
        return createCallback(pClass, tClass, idColumn, null);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn, String lookup){

        Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> callback = new Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>>() {

            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<P, T> prCellDataFeatures) {
                P bean = prCellDataFeatures.getValue();
                // TODO: not only String in cell!
                ItemPropertyBuilder ipb = ItemPropertyBuilder.create(String.class).bean(bean).field(idColumn);
                if( lookup != null ){
                    ipb = ipb.lookup(lookup);
                }
                ObservableValue<T> property = ipb.build();
                return property;
            }
        };
        return callback;
    }

}
