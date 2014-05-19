package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * User: tiziano
 * Date: 07/04/14
 * Time: 17:22
 */
public class ItemListProperty<E> extends ListPropertyBase<E> implements ZoeFXProperty<ObservableList<E>> {

    private BeanAccess<E> beanAccess;

    public ItemListProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
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
        System.out.println("--");
        //beanProperty.setValue(s);
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }
}
