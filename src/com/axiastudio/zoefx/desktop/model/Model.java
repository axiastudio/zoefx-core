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

package com.axiastudio.zoefx.desktop.model;

import com.axiastudio.zoefx.desktop.model.property.*;
import javafx.beans.property.Property;
import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 13:00
 */
public class Model<E> {

    private E entity;
    private Map<String, ZoeFXProperty> propertiesCache = new HashMap();
    private Map<String, Callback> callbacksCache = new HashMap();

    public Model(E entity) {
        this.entity = entity;
    }

    public ZoeFXProperty getProperty(String name){
        if( propertiesCache.containsKey(name) ){
            return propertiesCache.get(name);
        }
        return null;
    }

    public ZoeFXProperty getProperty(String name, Class<?> klass){
        if( propertiesCache.containsKey(name) ){
            return propertiesCache.get(name);
        }
        ZoeFXProperty property = ItemPropertyBuilder.create(klass).bean(entity).field(name).build();
        propertiesCache.put(name, property);
        return property;
    }

    public Callback getCallback(String name, String columnId) {
        return getCallback(name, columnId, null);
    }

    public Callback getCallback(String name, String columnId, String lookup) {
        String key = name+"."+columnId;
        if( callbacksCache.containsKey(key) ){
            return callbacksCache.get(key);
        }
        CallbackBuilder cb = CallbackBuilder.create().beanClass(entity.getClass()).field(key);
        if( lookup != null ){
            cb = cb.lookup(lookup);
        }
        Callback callback = cb.build();
        return callback;
    }

    public Class<? extends Object> getEntityClass() {
        return entity.getClass();
    }

    public E getEntity() {
        return entity;
    }

    @Override
    protected void finalize() throws Throwable {
        for( Property property: propertiesCache.values() ){
            property.unbind();
        }
    }

}
