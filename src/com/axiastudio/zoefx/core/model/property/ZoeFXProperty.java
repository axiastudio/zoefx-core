package com.axiastudio.zoefx.core.model.property;

import javafx.beans.property.Property;

/**
 * User: tiziano
 * Date: 19/05/14
 * Time: 15:24
 */
public interface ZoeFXProperty<T> extends Property<T> {

    public void refresh();

}
