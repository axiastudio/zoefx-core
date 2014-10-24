package com.axiastudio.zoefx.core.model.property;

import com.axiastudio.zoefx.core.model.BeanAccess;
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
    public void set(ObservableList observableList) {
        beanAccess.setValue(observableList);
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }
}
