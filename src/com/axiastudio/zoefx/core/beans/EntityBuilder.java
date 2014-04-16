package com.axiastudio.zoefx.core.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 16/04/14
 * Time: 11:42
 */
public class EntityBuilder<T> {

    private Class<? extends T> entityClass;
    private Map<String, Object> attributes = new HashMap<String, Object>();

    public EntityBuilder() {
    }

    public static <T> EntityBuilder<T> create(Class<? extends T> klass){
        EntityBuilder<T> entityBuilder = new EntityBuilder<>();
        entityBuilder.entityClass = klass;
        return entityBuilder;
    }

    public EntityBuilder<T> set(String name, Object value){
        attributes.put(name, value);
        return this;
    }

    public T build(){
        try {
            T bean = entityClass.newInstance();
            for( String name: attributes.keySet() ){
                Object value = attributes.get(name);
                BeanAccess<T> beanAccess = new BeanAccess<T>(bean, name);
                beanAccess.setValue(value);
            }
            return bean;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
