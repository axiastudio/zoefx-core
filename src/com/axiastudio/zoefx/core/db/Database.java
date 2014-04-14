package com.axiastudio.zoefx.core.db;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:49
 */
public interface Database {

    void open(String persistenceUnit);
    public <E> Manager<E> createManager(Class<E> klass);
}
