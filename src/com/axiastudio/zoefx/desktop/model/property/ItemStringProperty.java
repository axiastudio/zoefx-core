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

package com.axiastudio.zoefx.desktop.model.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.StringPropertyBase;
import javafx.util.Callback;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemStringProperty<P> extends StringPropertyBase implements ZoeFXProperty<String> {

    private BeanAccess<P> beanAccess;
    private Callback<P, String> toStringFunction =null;
    private Callback<String, P> fromStringFuction =null;

    public ItemStringProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    @Override
    public Object getBean() {
        return beanAccess.getBean();
    }

    @Override
    public String getName() {
        return beanAccess.getName();
    }

    @Override
    public String get() {
        P value = beanAccess.getValue();
        if( value instanceof String ) {
            return (String) value;
        } else if( value != null ) {
            return toStringFunction.call(value);
        }
        return null;
    }

    @Override
    public void set(String s) {
        Class<?> returnType = beanAccess.getReturnType();
        if( returnType == String.class ){
            beanAccess.setValue(s);
        } else {
            P value = fromStringFuction.call(s);
            if( value != null ) {
                beanAccess.setValue(value);
            }
        }
    }

    public void setToStringFunction(Callback<P, String> callback) {
        toStringFunction = callback;
    }

    public void setFromStringFunction(Callback<String, P> callback) {
        fromStringFuction = callback;
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }

}
