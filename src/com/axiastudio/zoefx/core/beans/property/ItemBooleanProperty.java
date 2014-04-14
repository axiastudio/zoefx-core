package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.BooleanPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemBooleanProperty extends BooleanPropertyBase {

    private BeanAccess<Boolean> beanAccess;

    public ItemBooleanProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemBooleanProperty(Object bean, String name) {
        beanAccess = new BeanAccess(bean, name);
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
        return beanAccess.getValue();
    }

    @Override
    public void set(boolean b) {
        beanAccess.setValue(b);
    }

}
