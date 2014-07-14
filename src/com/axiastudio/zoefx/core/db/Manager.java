package com.axiastudio.zoefx.core.db;

import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 21:49
 */
public interface Manager<E> {

    E commit(E entity);

    void commit(List<E> entities);

    void delete(E entity);

    void deleteRow(Object row);

    void truncate();

    E get(Long id);

    List<E> getAll();

    List<E> query(Map<String, Object> map);

    E create();

    Object createRow(String collectionName);

}
