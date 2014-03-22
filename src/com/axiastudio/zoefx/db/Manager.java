package com.axiastudio.zoefx.db;

import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 21:49
 */
public interface Manager<T> {

    T commit(T entity);

    void delete(T entity);

    void truncate();

    T get(Long id);

    List<T> getAll();

}
