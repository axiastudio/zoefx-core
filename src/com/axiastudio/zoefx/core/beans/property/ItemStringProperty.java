package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.StringPropertyBase;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemStringProperty extends StringPropertyBase {

    private BeanAccess<String> beanAccess;

    public ItemStringProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemStringProperty(Object bean, String name) {
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
    public String get() {
        return beanAccess.getValue();
    }

    @Override
    public void set(String s) {
        beanAccess.setValue(s);
    }
}
