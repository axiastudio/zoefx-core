package com.axiastudio.zoefx.core.model.beans;

import javafx.util.StringConverter;

/**
 * User: tiziano
 * Date: 03/08/14
 * Time: 13:06
 */
public class LookupStringConverter<T> extends StringConverter<T> {

    private String name;

    public LookupStringConverter(String name) {
        this.name = name;
    }

    @Override
    public String toString(T bean) {
        if( bean.getClass().isEnum() ){
            return bean.toString();
        } else {
            BeanAccess<String> beanAccess = new BeanAccess<>(bean, name);
            return beanAccess.getValue();
        }
    }

    @Override
    public T fromString(String string) {
        // not required for ChoiceBox
        return null;
    }
    
}
