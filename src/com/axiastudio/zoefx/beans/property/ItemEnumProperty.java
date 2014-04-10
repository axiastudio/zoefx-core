package com.axiastudio.zoefx.beans.property;

import com.axiastudio.zoefx.beans.BeanAccess;
import javafx.beans.property.ObjectPropertyBase;

import java.util.ArrayList;
import java.util.List;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemEnumProperty extends ObjectPropertyBase {

    private BeanAccess<Enum> beanAccess;

    public ItemEnumProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemEnumProperty(Object bean, String name) {
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
    public Object get() {
        return beanAccess.getValue();
    }

    @Override
    public void set(Object e) {
        beanAccess.setValue(e);
    }

    public List<Enum> getEnumConstants() {
        List<Enum> enums = new ArrayList();
        for( Object obj: beanAccess.getValue().getDeclaringClass().getEnumConstants() ){
            enums.add((Enum) obj);
        }
        return enums;
    }


}
