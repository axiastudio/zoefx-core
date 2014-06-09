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

    private Class beanClass;
    private String collectionName=null;
    private String fieldName =null;
    private String lookup=null;


    public CallbackBuilder() {
    }

    public static CallbackBuilder create(){
        return new CallbackBuilder();
    }

    public CallbackBuilder beanClass(Class klass){
        this.beanClass = klass; // es. Person
        return  this;
    }

    public CallbackBuilder field(String name){
        String[] split = name.split("\\.");
        if( split.length == 1 ){
            fieldName = split[0];
            collectionName = null;
        } else if( split.length == 2 ){
            collectionName = split[0]; // ex. loans
            fieldName = split[1]; // ex. book
        }
        return this;
    }

    public CallbackBuilder lookup(String lookup){
        this.lookup = lookup; // ex. title -> Person.loans[i].book.title
        return this;
    }

    public Callback build(){
        Class<?> pClass;
        if( collectionName != null ) {
            BeanClassAccess beanClassAccess = new BeanClassAccess(beanClass, collectionName);
            pClass = beanClassAccess.getGenericReturnType();
        } else {
            pClass = beanClass;

        }
        BeanClassAccess beanPropertyClassAccess = new BeanClassAccess(pClass, fieldName);
        Class<?> tClass = beanPropertyClassAccess.getReturnType();
        return createCallback(pClass, tClass, fieldName, lookup);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn){
        return createCallback(pClass, tClass, idColumn, null);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn, String lookup){

        Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> callback = new Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>>() {

            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<P, T> ptCellDataFeatures) {
                P bean = ptCellDataFeatures.getValue();
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
