package com.axiastudio.zoefx.core.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class NoPersistenceDatabaseImpl implements Database {

    Map<Class, NoPersistenceDatabaseManagerImpl> managers = new HashMap<>();

    @Override
    public void open(String persistenceUnit) {

    }

    @Override
    public void open(String persistenceUnit, Map<String, String> properties) {

    }

    @Override
    public <E> Manager<E> createManager(Class<E> klass) {
        if( !managers.keySet().contains(klass) ) {
            NoPersistenceDatabaseManagerImpl<E> manager = new NoPersistenceDatabaseManagerImpl<>(klass);
            managers.put(klass, manager);
        }
        return managers.get(klass);
    }

    @Override
    public <E> Manager<E> createManager(Class<E> klass, Manager<?> manager) {
        return createManager(klass);
    }

    /*
     *  Without Database store
     */

    public <E> void putStore(List<E> store, Class<E> klass){
        NoPersistenceDatabaseManagerImpl<E> manager = new NoPersistenceDatabaseManagerImpl<>(store);
        managers.put(klass, manager);
    }

}
