package com.axiastudio.zoefx.core.db;

import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 21:49
 */
public interface Manager<E> {

    E commit(E entity);

    void delete(E entity);

    void truncate();

    E get(Long id);

    List<E> getAll();

    E create();

}
