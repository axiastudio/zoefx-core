package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.beans.BeanClassAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class NoPersistenceDatabaseManagerImpl<E> implements Manager<E> {

    private List<E> store;
    private Class<E> entityClass;

    public NoPersistenceDatabaseManagerImpl(Class entityClass) {
        this.entityClass = entityClass;
        this.store = new ArrayList<E>();
    }

    public NoPersistenceDatabaseManagerImpl(List<E> store) {
        this.store = store;
        entityClass = (Class<E>) store.get(0).getClass();
    }

    @Override
    public E commit(E entity) {
        store.add(entity);
        return entity;
    }

    @Override
    public void commit(List<E> entities) {
        for( E entity: entities ){
            store.add(entity);
        }
    }

    @Override
    public void delete(E entity) {
        store.remove(entity);
    }

    @Override
    public void deleteRow(Object row) {
        // nothing to do
    }

    @Override
    public void truncate() {
        store.clear();

    }

    @Override
    public E get(Long id) {
        return store.get(id.intValue());
    }

    @Override
    public List<E> getAll() {
        return store;
    }

    @Override
    public List<E> query(Map<String, Object> map) {
        // not implemented
        return getAll();
    }

    @Override
    public E create() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createRow(String collectionName) {
        BeanClassAccess beanClassAccess = new BeanClassAccess(entityClass, collectionName);
        Class<?> genericReturnType = beanClassAccess.getGenericReturnType();
        try {
            return genericReturnType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}