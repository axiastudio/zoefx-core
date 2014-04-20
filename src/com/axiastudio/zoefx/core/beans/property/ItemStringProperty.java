package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.StringPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemStringProperty<P> extends StringPropertyBase {

    private BeanAccess<P> beanAccess;

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
        } else if( value instanceof Integer ) {
            return value.toString();
        }
        return null;
    }

    @Override
    public void set(String s) {
        Class<?> returnType = beanAccess.getReturnType();
        if( returnType == String.class ){
            beanAccess.setValue(s);
        } else if( returnType == Integer.class ){
            beanAccess.setValue(Integer.parseInt(s));
        }
    }
}
