package com.axiastudio.zoefx.core.events;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * User: tiziano
 * Date: 05/05/14
 * Time: 16:02
 */
public class ModelEvent extends Event {

    public static final EventType<ModelEvent> REFRESH = new EventType(ANY, "REFRESH");

    public ModelEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
        super(eventType);
    }

    public ModelEvent(@NamedArg("source") Object o, @NamedArg("target") EventTarget eventTarget, @NamedArg("eventType") EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
