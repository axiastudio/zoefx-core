/*
 * Copyright (c) 2015, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
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

package com.axiastudio.zoefx.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 07/01/15
 * Time: 13:17
 */
public abstract class AbstractManager<E> implements Manager<E>{

    @Override
    public List<E> getAll() {
        return query();
    }

    @Override
    public List<E> query(Long limit) {
        return null;
    }

    @Override
    public List<E> query(String orderby) {
        return query(new HashMap<>(), orderByList(orderby), Boolean.FALSE, null);
    }

    @Override
    public List<E> query(String orderby, Boolean reverse) {
        return query(new HashMap<>(), orderByList(orderby), reverse, null);
    }

    @Override
    public List<E> query(List<String> orderby) {
        return query(new HashMap<>(), orderby, Boolean.FALSE, null);
    }

    @Override
    public List<E> query(List<String> orderby, Boolean reverse) {
        return query(new HashMap<>(), orderby, reverse, null);
    }

    @Override
    public List<E> query(String orderby, Long limit) {
        return query(new HashMap<>(), orderByList(orderby), Boolean.FALSE, limit);
    }

    @Override
    public List<E> query(List<String> orderby, Long limit) {
        return query(new HashMap<>(), orderby, Boolean.FALSE, limit);
    }

    @Override
    public List<E> query(String orderby, Boolean reverse, Long limit) {
        return query(new HashMap<>(), orderByList(orderby), reverse, limit);
    }

    @Override
    public List<E> query(List<String> orderby, Boolean reverse, Long limit) {
        return query(new HashMap<>(), orderby, reverse, limit);
    }

    @Override
    public List<E> query(Map<String, Object> map) {
        return query(map, new ArrayList<>(), Boolean.FALSE, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, Long limit) {
        return query(map, new ArrayList<>(), Boolean.FALSE, limit);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby) {
        return query(map, orderByList(orderby), Boolean.FALSE, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Boolean reverse) {
        return query(map, orderByList(orderby), reverse, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby) {
        return query(map, orderby, Boolean.FALSE, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby, Boolean reverse) {
        return query(map, orderby, reverse, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Long limit) {
        return query(map, orderByList(orderby), Boolean.FALSE, limit);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby, Long limit) {
        return query(map, orderby, Boolean.FALSE, limit);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Boolean reverse, Long limit) {
        return query(map, orderByList(orderby), reverse, limit);
    }

    /*
    *  implemented in non-abstract class:
    *  public List<E> query(Map<String, Object> map, List<String> orderby, Boolean reverse, Long limit)
    */

    private List<String> orderByList(String orderby) {
        List<String> orderbys = new ArrayList<>();
        orderbys.add(orderby);
        return orderbys;
    }

}
