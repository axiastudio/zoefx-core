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

package com.axiastudio.zoefx.core.validators;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 16/04/14
 * Time: 10:02
 */
public class Validators {

    private static Map<String, Map<String, Validator>> validators = new HashMap<>();

    public static synchronized void bindValidator(Class klass, String name, Validator validator){
        Map<String, Validator> map = getClassMap(klass);
        map.put(name, validator);
    }

    public static Validator getValidator(Class klass, String name){
        Map<String, Validator> map = getClassMap(klass);
        return map.get(name);
    }

    private static synchronized Map<String, Validator> getClassMap(Class klass){
        Map<String, Validator> map;
        if( validators.keySet().contains(klass.getSimpleName()) ){
            map = validators.get(klass.getSimpleName());
        } else {
            map = new HashMap<>();
            validators.put(klass.getSimpleName(), map);
        }
        return map;
    }
}
