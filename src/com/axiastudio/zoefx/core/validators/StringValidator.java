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

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.script.JSEngineImpl;
import com.axiastudio.zoefx.core.script.ScriptEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 15/04/14
 * Time: 16:06
 */
public class StringValidator extends AbstractValidator implements Validator<String> {

    private Integer minLength=0;
    private Integer maxLength=-1;
    private Boolean notNull=false;

    public Boolean validate(String value) {
        if( value == null ){
            if( notNull ) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        if( !super.validate(value) ){
            return Boolean.FALSE;
        }
        if( value.length()<minLength ){
            return Boolean.FALSE;
        }
        if( maxLength>-1 && value.length()>maxLength ){
            return Boolean.FALSE;
        }
        if( code != null ) {
            ScriptEngine engine = Utilities.queryUtility(ScriptEngine.class);
            if( engine == null ){
                engine = new JSEngineImpl();
            }
            Map<String, Object> bindings = new HashMap<>();
            bindings.put("value", value);
            Boolean res = (Boolean) engine.eval(code, bindings);
            if( !res ){
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
}
