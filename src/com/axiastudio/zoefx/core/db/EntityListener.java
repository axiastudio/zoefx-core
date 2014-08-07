package com.axiastudio.zoefx.core.db;


/**
 * User: tiziano
 * Date: 07/08/14
 * Time: 14:10
 */
public interface EntityListener<E> {

    default void beforeCommit(E entity){}

}
