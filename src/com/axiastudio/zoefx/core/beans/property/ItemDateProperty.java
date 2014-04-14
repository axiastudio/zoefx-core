package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.core.beans.BeanAccess;
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

    private BeanAccess<Date> beanAccess;

    public ItemDateProperty(BeanAccess beanAccess){
        this.beanAccess = beanAccess;
    }

    public ItemDateProperty(Object bean, String name) {
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
        Date date = beanAccess.getValue();
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
            beanAccess.setValue(null);
            return;
        }
        Instant instant = ((LocalDate) localDate).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);
        beanAccess.setValue(date);
    }

}
