package com.axiastudio.zoefx.core.model.property;

import com.axiastudio.zoefx.core.model.BeanAccess;
import javafx.beans.property.BooleanPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemBooleanProperty<P> extends BooleanPropertyBase implements ZoeFXProperty<Boolean> {

    private BeanAccess<P> beanAccess;

    public ItemBooleanProperty(BeanAccess beanAccess){
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
    public boolean get() {
        P value = beanAccess.getValue();
        if( value instanceof Boolean ) {
            return (Boolean) value;
        } else if( value instanceof Integer ) {
            return false;
        }
        return false;
    }

    @Override
    public void set(boolean b) {
        beanAccess.setValue(b);
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }

}
