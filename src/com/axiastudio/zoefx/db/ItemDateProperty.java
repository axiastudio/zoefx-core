package com.axiastudio.zoefx.db;

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
public class ItemDateProperty extends ObjectPropertyBase {

    private BeanProperty<Date> beanProperty;

    public ItemDateProperty(Object bean, String name) {
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
    public Object get() {
        Date date = beanProperty.getValue();
        if( date == null ){
            return null;
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    @Override
    public void set(Object localDate) {
        if( localDate == null ){
            beanProperty.setValue(null);
            return;
        }
        Instant instant = ((LocalDate) localDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        beanProperty.setValue(date);
    }

}
