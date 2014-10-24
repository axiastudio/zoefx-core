package com.axiastudio.zoefx.core.model.property;

import com.axiastudio.zoefx.core.model.BeanAccess;
import javafx.beans.property.ObjectPropertyBase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 12:51
 */
public class ItemDateProperty<P> extends ObjectPropertyBase implements ZoeFXProperty {

    private BeanAccess<P> beanAccess;

    public ItemDateProperty(BeanAccess beanAccess){
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
        P value = beanAccess.getValue();
        if( value == null ){
            return null;
        }
        if( value instanceof Date ) {
            Instant instant = Instant.ofEpochMilli(((Date) value).getTime());
            LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
            return localDate;
        }
        return null;
    }

    @Override
    public void set(Object localDate) {
        if( localDate == null ){
            beanAccess.setValue(null);
            return;
        }
        Instant instant = ((LocalDate) localDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        beanAccess.setValue(date);
    }

    @Override
    public void refresh() {
        fireValueChangedEvent();
    }

}
