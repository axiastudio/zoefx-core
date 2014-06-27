package com.axiastudio.zoefx.core.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class WithoutDatabaseImpl implements Database {

    Map<Class, WithoutDatabaseManagerImpl> managers = new HashMap<>();

    @Override
    public void open(String persistenceUnit) {

    }

    @Override
    public void open(String persistenceUnit, Map<String, String> properties) {

    }

    @Override
    public <E> Manager<E> createManager(Class<E> klass) {
        return managers.get(klass);
    }

    /*
     *  Without Database store
     */
    public <E> void putStore(List<E> store, Class<E> klass){
        WithoutDatabaseManagerImpl<E> manager = new WithoutDatabaseManagerImpl<>(store);
        managers.put(klass, manager);
    }

}
