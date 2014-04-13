package com.axiastudio.zoefx.beans;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 13/04/14
 * Time: 14:56
 */
public class BeanClassAccess {
    protected String name;
    protected AccessType accessType;
    protected Field field;
    protected Method getter=null;
    protected Method setter=null;
    protected Class<?> beanClass;
    private Class<?> returnType;

    public BeanClassAccess(Class beanClass, String name) {
        this.name = name;
        this.beanClass = beanClass;
        inspectBeanProperty();
    }

    protected void inspectBeanProperty() {
        Boolean getterOk=Boolean.FALSE;

        // getter
        String getterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        try {
            getter = beanClass.getMethod(getterName);
            returnType = getter.getReturnType();
            getterOk = Boolean.TRUE;
        } catch (NoSuchMethodException e) {

        }
        // setter
        String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        if( getterOk ) {
            try {
                setter = beanClass.getMethod(setterName, returnType);
                accessType = AccessType.METHOD;
                return;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        // try to access the field
        try {
            field = beanClass.getField(name);
            returnType = field.getType();
            accessType = AccessType.FIELD;
            return;
        } catch (NoSuchFieldException e) {

        }

    }

    public Class<?> getGenericReturnType() {
        if( Collection.class.isAssignableFrom(returnType) ){
            if( accessType.equals(AccessType.FIELD) ){
                Field field = null;
                try {
                    field = beanClass.getField(name);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                Type type = field.getGenericType();
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Class pClass = (Class) actualTypeArguments[0];
                return pClass;
            } else if( accessType.equals(AccessType.METHOD) ){
                try {
                    Method method = beanClass.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                    ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
                    Type[] actualTypeArguments = pt.getActualTypeArguments();
                    Class pClass = (Class) actualTypeArguments[0];
                    return pClass;
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public String getName() {
        return name;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public AccessType getAccessType() {
        return accessType;
    }
}
