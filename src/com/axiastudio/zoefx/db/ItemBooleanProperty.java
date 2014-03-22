package com.axiastudio.zoefx.db;

import javafx.beans.property.BooleanPropertyBase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemBooleanProperty extends BooleanPropertyBase {

    private Object bean;
    private String name;
    private Field field;
    private Method getter=null;
    private Method setter=null;

    public ItemBooleanProperty(Object bean, String name) {
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
    public Boolean getValue() {
        try {
            return (Boolean) field.get(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValue(Boolean bean) {
        //super.setValue(s);
    }
}
