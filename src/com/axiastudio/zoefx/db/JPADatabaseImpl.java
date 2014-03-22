package com.axiastudio.zoefx.db;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:46
 */
public class JPADatabaseImpl implements Database {

    private EntityManagerFactory entityManagerFactory;

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Initialize the entity manager factory.
     *
     * @param persistenceUnit The persistence unit defined in persistence.xml
     *
     */
    @Override
    public void open(String persistenceUnit) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
    }

    /**
     * Create a controller for the entities of the given class.
     *
     * @param klass The class managed from the controller
     *
     */
    @Override
    public <T> Manager<T> createController(Class<T> klass){
        JPAManagerImpl<T> controller = new JPAManagerImpl(getEntityManagerFactory().createEntityManager(), klass);
        return controller;
    }
}