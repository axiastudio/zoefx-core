/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.axiastudio.zoefx.desktop.db;

import com.axiastudio.zoefx.core.db.AbstractManager;
import com.axiastudio.zoefx.core.db.Manager;
import com.axiastudio.zoefx.core.beans.BeanClassAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class NoPersistenceDatabaseManagerImpl<E> extends AbstractManager<E> implements Manager<E> {

    private List<E> store;
    private Class<E> entityClass;

    public NoPersistenceDatabaseManagerImpl(Class entityClass) {
        this.entityClass = entityClass;
        this.store = new ArrayList<E>();
    }

    public NoPersistenceDatabaseManagerImpl(List<E> store) {
        this.store = store;
        entityClass = (Class<E>) store.get(0).getClass();
    }

    @Override
    public E save(E entity) {
        if( !store.contains(entity) ) {
            store.add(entity);
        }
        return entity;
    }

    @Override
    public void save(List<E> entities) {
        for( E entity: entities ){
            store.add(entity);
        }
    }

    @Override
    public void delete(E entity) {
        store.remove(entity);
    }

    @Override
    public void deleteRow(Object row) {
        // nothing to do
    }

    @Override
    public void truncate() {
        store.clear();

    }

    @Override
    public E get(Long id) {
        return store.get(id.intValue());
    }

    @Override
    public List<E> query() {
        return store;
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby, List<Boolean> reverse, Integer size, Integer startindex) {
        // not implemented
        return query();
    }

    @Override
    public E create() {
        try {
            return entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createRow(String collectionName) {
        BeanClassAccess beanClassAccess = new BeanClassAccess(entityClass, collectionName);
        Class<?> genericReturnType = beanClassAccess.getGenericReturnType();
        try {
            return genericReturnType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


}
