package com.axiastudio.zoefx.beans.property;

import com.axiastudio.zoefx.beans.BeanAccess;
import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * User: tiziano
 * Date: 07/04/14
 * Time: 17:22
 */
public class ItemListProperty<E> extends ListPropertyBase<E> {

    private BeanAccess<E> beanAccess;

    public ItemListProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemListProperty(E bean, String name) {
        beanAccess = new BeanAccess(bean, name);
    }

    @Override
    public Object getBean() {
        return beanAccess.getBean();
    }

    @Override
    public String getName() {
        return beanAccess.getName();
    }

    @Override
    public ObservableList get() {
        return FXCollections.observableArrayList((Collection) beanAccess.getValue());
    }

    @Override
    public void set(ObservableList es) {
        //beanProperty.setValue(s);
    }
}
