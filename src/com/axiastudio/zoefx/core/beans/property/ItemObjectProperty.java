package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import javafx.beans.property.ObjectPropertyBase;

import java.util.ArrayList;
import java.util.List;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemObjectProperty<P> extends ObjectPropertyBase {

    private BeanAccess<P> beanAccess;

    public ItemObjectProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemObjectProperty(Object bean, String name) {
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

    public List<P> getSuperset() {
        List<P> superset = new ArrayList();
        if( beanAccess.getReturnType().isEnum() ) {
            for (Object obj : ((Enum) beanAccess.getValue()).getDeclaringClass().getEnumConstants()) {
                superset.add((P) obj);
            }
        } else {
            System.out.println("-> " + beanAccess.getReturnType());
        }
        return superset;
    }


}
