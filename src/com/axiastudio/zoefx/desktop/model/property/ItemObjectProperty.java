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

package com.axiastudio.zoefx.desktop.model.property;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.core.beans.BeanAccess;
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

    public List<P> getSuperset(Manager<?> parentManager) {
        List<P> superset = new ArrayList();
        Class<?> returnType = beanAccess.getReturnType();
        if( returnType.isEnum() ) {
            for (Object obj : ((Enum) beanAccess.getValue()).getDeclaringClass().getEnumConstants()) {
                superset.add((P) obj);
            }
        } else {
            Database database = IOC.queryUtility(Database.class);
            if( database != null ) {
                Manager<?> manager = database.createManager(returnType, parentManager);
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
