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

import com.axiastudio.zoefx.core.db.Database;
import com.axiastudio.zoefx.core.db.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 27/06/14
 * Time: 10:33
 */
public class NoPersistenceDatabaseImpl implements Database {

    Map<Class, NoPersistenceDatabaseManagerImpl> managers = new HashMap<>();

    @Override
    public void open(String persistenceUnit) {

    }

    @Override
    public void open(String persistenceUnit, Map<String, String> properties) {

    }

    @Override
    public <E> Manager<E> createManager(Class<E> klass) {
        if( !managers.keySet().contains(klass) ) {
            NoPersistenceDatabaseManagerImpl<E> manager = new NoPersistenceDatabaseManagerImpl<>(klass);
            managers.put(klass, manager);
        }
        return managers.get(klass);
    }

    @Override
    public <E> Manager<E> createManager(Class<E> klass, Manager<?> manager) {
        return createManager(klass);
    }

    /*
     *  Without Database store
     */

    public <E> void putStore(List<E> store, Class<E> klass){
        NoPersistenceDatabaseManagerImpl<E> manager = new NoPersistenceDatabaseManagerImpl<>(store);
        managers.put(klass, manager);
    }

}
