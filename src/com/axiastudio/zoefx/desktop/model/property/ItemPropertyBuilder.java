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
import com.axiastudio.zoefx.desktop.model.converters.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

/**
 * User: tiziano
 * Date: 10/04/14
 * Time: 11:08
 */
public class ItemPropertyBuilder<T> {

    private Object bean;
    private String name;
    private String lookup=null;
    private Class<? extends T> propertyClass;

    public ItemPropertyBuilder() {
    }

    /*
    public static ItemPropertyBuilder create(){
        return new ItemPropertyBuilder();
    }
    */

    public static <T> ItemPropertyBuilder<T> create(Class<? extends T> klass){
        ItemPropertyBuilder<T> itemPropertyBuilder = new ItemPropertyBuilder<T>();
        itemPropertyBuilder.propertyClass = klass;
        return itemPropertyBuilder;
    }

    public ItemPropertyBuilder bean(Object bean){
        this.bean = bean;
        return this;
    }

    public ItemPropertyBuilder field(String name){
        this.name = name;
        return this;
    }

    public ItemPropertyBuilder lookup(String lookup){
        this.lookup = lookup;
        return this;
    }

    public ZoeFXProperty build(){
        BeanAccess beanAccess = new BeanAccess(bean, name);
        Class<?> fieldType = beanAccess.getReturnType();
        if( fieldType == null || propertyClass == null ){
            return null;
        }
        //System.out.println(bean.getClass().getSimpleName() + "." + name + " (" + fieldType + ") -> property (" + propertyClass + ")");
        if( String.class.isAssignableFrom(propertyClass) ) {
            if( String.class.isAssignableFrom(fieldType) ) {
                // String field -> String property
                ItemStringProperty<String> item = new ItemStringProperty(beanAccess);
                return item;
            } else if( Integer.class.isAssignableFrom(fieldType) ) {
                // Integer field -> String property
                ItemStringProperty<Integer> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(Object::toString);
                item.setFromStringFunction(new String2Integer());
                return item;
            } else if( Long.class.isAssignableFrom(fieldType) ) {
                // Long field -> String property
                ItemStringProperty<Long> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(Object::toString);
                item.setFromStringFunction(new String2Long());
                return item;
            } else if( Double.class.isAssignableFrom(fieldType) ) {
                // Double field -> String property
                ItemStringProperty<Double> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(Object::toString);
                item.setFromStringFunction(new String2Double());
                return item;
            } else if( BigDecimal.class.isAssignableFrom(fieldType) ) {
                // BigDecimal field -> String property
                ItemStringProperty<BigDecimal> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(new BigDecimal2String());
                item.setFromStringFunction(new String2BigDecimal());
                return item;
            } else if( Date.class.isAssignableFrom(fieldType) ){
                // Date field -> String property
                ItemStringProperty<Date> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(new Date2String());
                return item;
            } else if( Object.class.isAssignableFrom(fieldType) ){
                // Object field -> String property
                ItemStringProperty<Object> item = new ItemStringProperty(beanAccess);
                item.setToStringFunction(o -> {
                    if( lookup != null ) {
                        BeanAccess ba = new BeanAccess(o, lookup);
                        return (String) ba.getValue();
                    }
                    return o.toString();
                });
                return item;
            }
        } else if( Boolean.class.isAssignableFrom(propertyClass) ) {
            if( Boolean.class.isAssignableFrom(fieldType) ) {
                // Boolean field -> Boolean property
                ItemBooleanProperty<Boolean> item = new ItemBooleanProperty(beanAccess);
                return item;
            }
        } else if( Date.class.isAssignableFrom(propertyClass) ) {
            if( Date.class.isAssignableFrom(fieldType) ) {
                // Date field -> Date property
                ItemDateProperty<Date> item = new ItemDateProperty(beanAccess);
                return item;
            }
        } else if( Collection.class.isAssignableFrom(propertyClass) ) {
                // Collection field -> Collection property
                ItemListProperty item = new ItemListProperty(beanAccess);
                return item;
        } else if( Object.class.isAssignableFrom(propertyClass) ) {
            // Object field -> Object property
            ItemObjectProperty<Object> item = new ItemObjectProperty(beanAccess);
            return item;
        }
        return null;
    }

}
