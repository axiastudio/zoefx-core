package com.axiastudio.zoefx.core.events;

import java.util.EventListener;

/**
 * User: tiziano
 * Date: 26/05/14
 * Time: 11:08
 */
public interface DataSetEventListener extends EventListener {

    void dataSetEventHandler(DataSetEvent e);

}
