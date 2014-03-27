package com.axiastudio.zoefx.db;

import javafx.beans.property.ObjectPropertyBase;

import java.util.ArrayList;
import java.util.List;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemEnumProperty extends ObjectPropertyBase {

    private BeanProperty<Enum> beanProperty;

    public ItemEnumProperty(Object bean, String name) {
        beanProperty = new BeanProperty(bean, name);

    }

    @Override
    public Object getBean() {
        return beanProperty.getBean();
    }

    @Override
    public String getName() {
        return beanProperty.getName();
    }

    @Override
    public Object getValue() {
        return beanProperty.getValue();
    }

    @Override
    public void setValue(Object e) {
        //beanProperty.setValue(e);
    }

    public List<Enum> getEnumConstants() {
        List<Enum> enums = new ArrayList();
        for( Object obj: beanProperty.getValue().getDeclaringClass().getEnumConstants() ){
            enums.add((Enum) obj);
        }
        return enums;
    }
}
