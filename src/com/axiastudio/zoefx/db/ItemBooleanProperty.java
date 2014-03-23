package com.axiastudio.zoefx.db;

import javafx.beans.property.BooleanPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemBooleanProperty extends BooleanPropertyBase {

    private BeanProperty<Boolean> beanProperty;

    public ItemBooleanProperty(Object bean, String name) {
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
    public Boolean getValue() {
        return beanProperty.getValue();
    }

    @Override
    public void setValue(Boolean bean) {
        //super.setValue(s);
    }
}
