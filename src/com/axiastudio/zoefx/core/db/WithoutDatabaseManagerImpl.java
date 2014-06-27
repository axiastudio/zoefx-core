package com.axiastudio.zoefx.core.db;

import java.util.List;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class WithoutDatabaseManagerImpl<E> implements Manager<E> {

    private List<E> store;
    private Class<E> entityClass;

    public WithoutDatabaseManagerImpl(List<E> store) {
        this.store = store;
        entityClass = (Class<E>) store.get(0).getClass();
    }

    @Override
    public E commit(E entity) {
        return entity;
    }

    @Override
    public void commit(List<E> entities) {

    }

    @Override
    public void delete(E entity) {
        store.remove(entity);
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
}
