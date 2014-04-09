package com.axiastudio.zoefx.db;

import javafx.beans.property.ListPropertyBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.Collection;
import java.util.List;

/**
 * User: tiziano
 * Date: 07/04/14
 * Time: 17:22
 */
public class ItemListProperty<E> extends ListPropertyBase<E> {

    private BeanProperty<E> beanProperty;

    public ItemListProperty(E bean, String name) {
        beanProperty = new BeanProperty(bean, name);
    }

    @Override
    public Object getBean() {
        return beanProperty.getBean();
    }

    @Override
    public String getName() {
        return beanProperty.getName();
    }

    @Override
    public ObservableList get() {
        return FXCollections.observableArrayList((Collection) beanProperty.getValue());
    }

    @Override
    public void set(ObservableList es) {
        //beanProperty.setValue(s);
    }
}
