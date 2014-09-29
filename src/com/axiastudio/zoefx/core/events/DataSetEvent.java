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
public class DataSetEvent extends Event {

    public static final EventType<DataSetEvent> INDEX_CHANGED = new EventType(ANY, "INDEX_CHANGED");

    public static final EventType<DataSetEvent> GET_DIRTY = new EventType(ANY, "GET_DIRTY");

    public static final EventType<DataSetEvent> BEFORE_COMMIT = new EventType(ANY, "BEFORE_COMMIT");

    public static final EventType<DataSetEvent> COMMITED = new EventType(ANY, "COMMITED");

    public static final EventType<DataSetEvent> REVERTED = new EventType(ANY, "REVERTED");

    public static final EventType<DataSetEvent> CREATED = new EventType(ANY, "CREATE");

    public static final EventType<DataSetEvent> BEFORE_DELETE = new EventType(ANY, "BEFORE_DELETE");

    public static final EventType<DataSetEvent> DELETED = new EventType(ANY, "DELETED");

    public static final EventType<DataSetEvent> ROWS_CREATED = new EventType(ANY, "ROWS_CREATED");

    public static final EventType<DataSetEvent> ROWS_DELETED = new EventType(ANY, "ROWS_DELETED");

    public static final EventType<DataSetEvent> STORE_CHANGED = new EventType(ANY, "STORE_CHANGED");

    public DataSetEvent(@NamedArg("eventType") EventType<? extends Event> eventType) {
        super(eventType);
    }

    public DataSetEvent(@NamedArg("source") Object o, @NamedArg("target") EventTarget eventTarget, @NamedArg("eventType") EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
