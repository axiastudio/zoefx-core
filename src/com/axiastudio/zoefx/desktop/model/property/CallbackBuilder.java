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

import com.axiastudio.zoefx.core.beans.BeanClassAccess;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * User: tiziano
 * Date: 10/04/14
 * Time: 12:00
 */
public class CallbackBuilder {

    private Class beanClass;
    private String collectionName=null;
    private String fieldName =null;
    private String lookup=null;


    public CallbackBuilder() {
    }

    public static CallbackBuilder create(){
        return new CallbackBuilder();
    }

    public CallbackBuilder beanClass(Class klass){
        this.beanClass = klass; // es. Person
        return  this;
    }

    public CallbackBuilder field(String name){
        String[] split = name.split("\\.");
        if( split.length == 1 ){
            fieldName = split[0];
            collectionName = null;
        } else if( split.length == 2 ){
            collectionName = split[0]; // ex. loans
            fieldName = split[1]; // ex. book
        }
        return this;
    }

    public CallbackBuilder lookup(String lookup){
        this.lookup = lookup; // ex. title -> Person.loans[i].book.title
        return this;
    }

    public Callback build(){
        Class<?> pClass;
        if( collectionName != null ) {
            BeanClassAccess beanClassAccess = new BeanClassAccess(beanClass, collectionName);
            pClass = beanClassAccess.getGenericReturnType();
        } else {
            pClass = beanClass;

        }
        BeanClassAccess beanPropertyClassAccess = new BeanClassAccess(pClass, fieldName);
        Class<?> tClass = beanPropertyClassAccess.getReturnType();
        return createCallback(pClass, tClass, fieldName, lookup);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn){
        return createCallback(pClass, tClass, idColumn, null);
    }

    private <P, T> Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> createCallback(Class<P> pClass, Class<T> tClass, String idColumn, String lookup){

        Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>> callback = new Callback<TableColumn.CellDataFeatures<P, T>, ObservableValue<T>>() {

            @Override
            public ObservableValue<T> call(TableColumn.CellDataFeatures<P, T> ptCellDataFeatures) {
                P bean = ptCellDataFeatures.getValue();
                // TODO: not only String in cell!
                ItemPropertyBuilder ipb = ItemPropertyBuilder.create(String.class).bean(bean).field(idColumn);
                if( lookup != null ){
                    ipb = ipb.lookup(lookup);
                }
                ObservableValue<T> property = ipb.build();
                return property;
            }
        };
        return callback;
    }

}
