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
    public List<E> query(Integer size) {
        return query(new HashMap<>(), new ArrayList<>(), new ArrayList<>(), size);
    }

    @Override
    public List<E> query(String orderby) {
        return query(new HashMap<>(), orderByList(orderby), reverseList(Boolean.FALSE), null);
    }

    @Override
    public List<E> query(String orderby, Boolean reverse) {
        return query(new HashMap<>(), orderByList(orderby), reverseList(reverse), null);
    }

    @Override
    public List<E> query(List<String> orderby) {
        return query(new HashMap<>(), orderby, reverseList(Boolean.FALSE), null);
    }

    @Override
    public List<E> query(List<String> orderby, List<Boolean> reverse) {
        return query(new HashMap<>(), orderby, reverse, null);
    }

    @Override
    public List<E> query(String orderby, Integer size) {
        return query(new HashMap<>(), orderByList(orderby), reverseList(Boolean.FALSE), size);
    }

    @Override
    public List<E> query(List<String> orderby, Integer size) {
        return query(new HashMap<>(), orderby, reverseList(Boolean.FALSE), size);
    }

    @Override
    public List<E> query(String orderby, Boolean reverse, Integer size) {
        return query(new HashMap<>(), orderByList(orderby), reverseList(reverse), size);
    }

    @Override
    public List<E> query(List<String> orderby, List<Boolean> reverse, Integer size) {
        return query(new HashMap<>(), orderby, reverse, size);
    }

    @Override
    public List<E> query(Map<String, Object> map) {
        return query(map, new ArrayList<>(), reverseList(Boolean.FALSE), null);
    }

    @Override
    public List<E> query(Map<String, Object> map, Integer size) {
        return query(map, new ArrayList<>(), reverseList(Boolean.FALSE), size);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby) {
        return query(map, orderByList(orderby), reverseList(Boolean.FALSE), null);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Boolean reverse) {
        return query(map, orderByList(orderby), reverseList(reverse), null);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby) {
        return query(map, orderby, reverseList(Boolean.FALSE), null);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby, List<Boolean> reverse) {
        return query(map, orderby, reverse, null);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Integer size) {
        return query(map, orderByList(orderby), reverseList(Boolean.FALSE), size);
    }

    @Override
    public List<E> query(Map<String, Object> map, List<String> orderby, Integer size) {
        return query(map, orderby, reverseList(Boolean.FALSE), size);
    }

    @Override
    public List<E> query(Map<String, Object> map, String orderby, Boolean reverse, Integer size) {
        return query(map, orderByList(orderby), reverseList(reverse), size);
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

    private List<Boolean> reverseList(Boolean reverse) {
        List<Boolean> orderbys = new ArrayList<>();
        orderbys.add(reverse);
        return orderbys;
    }

}
