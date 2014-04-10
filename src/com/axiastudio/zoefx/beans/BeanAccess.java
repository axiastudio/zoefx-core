package com.axiastudio.zoefx.beans;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: tiziano
 * Date: 22/03/14
 * Time: 22:07
 */
public class BeanAccess<T> {

    private Object bean;
    private String name;
    private AccessType accessType;
    private Field field;
    private Method getter=null;
    private Method setter=null;
    private Class<?> type;

    public BeanAccess(Object bean, String name) {
        this.bean = bean;
        this.name = name;
        inspectBeanProperty(bean, name);
    }

    private void inspectBeanProperty(Object bean, String name) {
        Boolean getterOk=Boolean.FALSE;

        // getter
        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        try {
            getter = bean.getClass().getMethod(getterName);
            type = getter.getReturnType();
            getterOk = Boolean.TRUE;
        } catch (NoSuchMethodException e) {

        }
        // setter
        String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        if( getterOk ) {
            try {
                setter = bean.getClass().getMethod(setterName, type);
                accessType = AccessType.METHOD;
                return;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        // try to access the field
        try {
            field = bean.getClass().getField(name);
            type = field.getType();
            accessType = AccessType.FIELD;
            return;
        } catch (NoSuchFieldException e) {

        }

    }

    public Object getBean() {
        return bean;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public T getValue() {
        if( accessType.equals(AccessType.FIELD) ){
            try {
                return (T) field.get(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return (T) getter.invoke(bean);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setValue(Object value) {
        if( accessType.equals(AccessType.FIELD) ) {
            try {
                field.set(bean, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                setter.invoke(bean, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
