package com.axiastudio.zoefx.beans.property;

import com.axiastudio.zoefx.beans.BeanAccess;
import javafx.beans.property.Property;

import java.util.Collection;
import java.util.Date;

/**
 * User: tiziano
 * Date: 10/04/14
 * Time: 11:08
 */
public class ItemPropertyBuilder {

    private Object bean;
    private String propertyName;

    public ItemPropertyBuilder() {
    }

    public static ItemPropertyBuilder create(){
        return new ItemPropertyBuilder();
    }

    public ItemPropertyBuilder bean(Object bean){
        this.bean = bean;
        return  this;
    }

    public ItemPropertyBuilder property(String name){
        this.propertyName = name;
        return this;
    }

    public Property build(){
        BeanAccess beanAccess = new BeanAccess(bean, propertyName);
        Class type = beanAccess.getType();
        Property item=null;
        if( type == String.class ){
            item = new ItemStringProperty(beanAccess);
        } else if( type == Boolean.class ){
            item = new ItemBooleanProperty(beanAccess);
        } else if( type == Date.class ){
            item = new ItemDateProperty(beanAccess);
        } else if( type != null && type.isEnum() ){
            item = new ItemEnumProperty(beanAccess);
        } else if( type == Collection.class ){
            item = new ItemListProperty(beanAccess);
        }
        return item;
    }

}
