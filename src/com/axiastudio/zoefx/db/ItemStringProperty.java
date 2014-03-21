package com.axiastudio.zoefx.db;

import javafx.beans.property.StringPropertyBase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemStringProperty extends StringPropertyBase {

    private Object bean;
    private String name;
    private Field field;
    private Method getter=null;
    private Method setter=null;

    public ItemStringProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name;
        // field
        try {
            this.field = bean.getClass().getField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // getter
        // setter
    }

    @Override
    public Object getBean() {
        return bean;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        try {
            return (String) field.get(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValue(String s) {
        //super.setValue(s);
    }
}
