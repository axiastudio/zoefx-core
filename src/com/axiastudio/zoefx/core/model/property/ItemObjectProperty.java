package com.axiastudio.zoefx.core.model.property;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.model.BeanAccess;
import com.axiastudio.zoefx.core.db.Database;
import com.axiastudio.zoefx.core.db.Manager;
import javafx.beans.property.ObjectPropertyBase;

import java.util.ArrayList;
import java.util.List;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemObjectProperty<P> extends ObjectPropertyBase implements ZoeFXProperty {

    private BeanAccess<P> beanAccess;

    public ItemObjectProperty(BeanAccess beanAccess){
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
    public Object get() {
        return beanAccess.getValue();
    }

    @Override
    public void set(Object e) {
        beanAccess.setValue(e);
    }

    public List<P> getSuperset() {
        List<P> superset = new ArrayList();
        Class<?> returnType = beanAccess.getReturnType();
        if( returnType.isEnum() ) {
            for (Object obj : ((Enum) beanAccess.getValue()).getDeclaringClass().getEnumConstants()) {
                superset.add((P) obj);
            }
        } else {
            Database database = Utilities.queryUtility(Database.class);
            if( database != null ) {
                Manager<?> manager = database.createManager(returnType);
                for (Object obj : manager.getAll()) {
                    superset.add((P) obj);
                }
            }
        }
        return superset;
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }
}
