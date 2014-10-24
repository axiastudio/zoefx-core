package com.axiastudio.zoefx.core.model.property;

import com.axiastudio.zoefx.core.model.beans.BeanAccess;
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
