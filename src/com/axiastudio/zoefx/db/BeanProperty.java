package com.axiastudio.zoefx.db;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * User: tiziano
 * Date: 22/03/14
 * Time: 22:07
 */
public class BeanProperty<T> {

    private Object bean;
    private String name;
    private Field field;
    private Method getter=null;
    private Method setter=null;

    public BeanProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name;
        inspectBeanProperty(bean, name);
    }

    private void inspectBeanProperty(Object bean, String name) {
        // field
        try {
            this.field = bean.getClass().getField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        // getter
        // setter
    }

    public Object getBean() {
        return bean;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        try {
            return (T) field.get(bean);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(Object value) {
        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
