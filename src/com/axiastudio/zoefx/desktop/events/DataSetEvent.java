/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.axiastudio.zoefx.desktop.events;

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
