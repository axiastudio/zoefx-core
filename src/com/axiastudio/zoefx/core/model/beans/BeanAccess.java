package com.axiastudio.zoefx.core.model.beans;

import java.lang.reflect.InvocationTargetException;

/**
 * User: tiziano
 * Date: 22/03/14
 * Time: 22:07
 */
public class BeanAccess<T> extends BeanClassAccess {

    private Object bean;

    public BeanAccess(Object bean, String name) {
        super(bean.getClass(), name);
        this.bean = bean;
    }

    public Object getBean() {
        return bean;
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
