package com.axiastudio.zoefx.core.db;

import java.util.Map;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:49
 */
public interface Database {

    void open(String persistenceUnit);
    void open(String persistenceUnit, Map<String, String> properties);
    public <E> Manager<E> createManager(Class<E> klass);
}
