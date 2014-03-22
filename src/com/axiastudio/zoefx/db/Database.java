package com.axiastudio.zoefx.db;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:49
 */
public interface Database {

    void open(String persistenceUnit);
    public <T> Manager<T> createController(Class<T> klass);
}
