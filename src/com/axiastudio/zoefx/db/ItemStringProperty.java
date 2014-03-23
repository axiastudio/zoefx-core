package com.axiastudio.zoefx.db;

import javafx.beans.property.StringPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemStringProperty extends StringPropertyBase {

    private BeanProperty<String> beanProperty;

    public ItemStringProperty(Object bean, String name) {
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
    public String getValue() {
        return beanProperty.getValue();
    }

    @Override
    public void setValue(String s) {
        //super.setValue(s);
    }
}
