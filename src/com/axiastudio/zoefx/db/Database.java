package com.axiastudio.zoefx.db;

import java.util.Map;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:49
 */
public interface Database {

    void open(String persistenceUnit);
    public <T> Controller<T> createController(Class<T> klass);
}
