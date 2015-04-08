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

package com.axiastudio.zoefx.desktop.db;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.core.db.Database;
import com.axiastudio.zoefx.core.db.EntityListener;
import com.axiastudio.zoefx.core.db.Manager;
import com.axiastudio.zoefx.core.beans.BeanAccess;
import com.axiastudio.zoefx.desktop.events.DataSetEvent;
import com.axiastudio.zoefx.desktop.events.DataSetEventGenerator;
import com.axiastudio.zoefx.desktop.events.DataSetEventListener;
import com.axiastudio.zoefx.desktop.model.Model;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 25/03/14
 * Time: 16:52
 */
public class DataSet<E> implements DataSetEventGenerator {

    private List<E> store;
    private Class<E> entityClass;
    private Manager<E> manager=null;
    private EntityListener<E> listener =null;
    private Integer currentIndex;
    private Model<E> currentModel=null;
    private Boolean dirty=Boolean.FALSE;
    private List<DataSetEventListener> dataSetEventListeners = new ArrayList<DataSetEventListener>();

    // access policy
    private SimpleBooleanProperty canSelect = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canInsert = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canUpdate = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canDelete = new SimpleBooleanProperty(Boolean.TRUE);


    public DataSet() {
    }

    public boolean getCanSelect() {
        return canSelect.get();
    }

    public SimpleBooleanProperty canSelectProperty() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect.set(canSelect);
    }

    public boolean getCanInsert() {
        return canInsert.get();
    }

    public SimpleBooleanProperty canInsertProperty() {
        return canInsert;
    }

    public void setCanInsert(boolean canInsert) {
        this.canInsert.set(canInsert);
    }

    public boolean getCanUpdate() {
        return canUpdate.get();
    }

    public SimpleBooleanProperty canUpdateProperty() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate.set(canUpdate);
    }

    public boolean getCanDelete() {
        return canDelete.get();
    }

    public SimpleBooleanProperty canDeleteProperty() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete.set(canDelete);
    }

    public void setStore(List<E> store) {
        this.store = store;
        goFirst();
        fireDataSetEvent(new DataSetEvent(DataSetEvent.STORE_CHANGED));
        dirty = Boolean.FALSE;
    }

    public List<E> getStore() {
        if( store == null ){
            store = manager.getAll();
        }
        return store;
    }

    public Integer getCurrentIndex() {
        return currentIndex;
    }

    public Model<E> newModel() {
        E entity = getStore().get(currentIndex);
        currentModel = new Model(entity);
        fireDataSetEvent(new DataSetEvent(DataSetEvent.INDEX_CHANGED));
        return currentModel;
    }

    public Model<E> getCurrentModel() {
        return currentModel;
    }

    public void goFirst() {
        currentIndex = 0;
    }

    public void goLast() {
        currentIndex = getStore().size()-1;
    }

    public void goNext() {
        if( currentIndex < getStore().size()-1 ){
            currentIndex++;
        }
    }

    public void goPrevious() {
        if( currentIndex>0 ){
            currentIndex--;
        }
    }

    public Integer size() {
        return getStore().size();
    }

    public Boolean isDirty() {
        return dirty;
    }

    public void getDirty() {
        if( !dirty ) {
            dirty = Boolean.TRUE;
            fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
        }
    }

    public void revert() {
        dirty = Boolean.FALSE;
        fireDataSetEvent(new DataSetEvent(DataSetEvent.REVERTED));
    }

    public void save() {
        fireDataSetEvent(new DataSetEvent(DataSetEvent.BEFORE_COMMIT));
        Manager<E> manager = getManager();
        if( manager != null ) {
            E entity = getStore().get(currentIndex);
            E merged = manager.save(entity);
            getStore().set(currentIndex, merged);
        }
        dirty = Boolean.FALSE;
        fireDataSetEvent(new DataSetEvent(DataSetEvent.COMMITED));
    }

    public Manager<E> getManager() {
        if( manager == null ) {
            Database db = IOC.queryUtility(Database.class);
            if (db != null) {
                manager = db.createManager(getEntityClass());
            }
        }
        return manager;
    }

    public void setManager(Manager<E> manager) {
        this.manager = manager;
    }

    public void setListener(EntityListener<E> listener) {
        this.listener = listener;
    }

    public Class<E> getEntityClass() {
        if( entityClass == null ){
            if( getStore().size()==0 ) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "The entity class is not defined and the size of the store is 0!");
            } else {
                return (Class<E>) getStore().get(0).getClass();
            }
        }
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public E create() {
        Manager<E> manager = getManager();
        if( manager != null ) {
            E entity = manager.create();
            getStore().add(entity);
            currentIndex = getStore().size()-1;
            fireDataSetEvent(new DataSetEvent(DataSetEvent.CREATED));
            return entity;
        } else {
            try {
                E entity = getEntityClass().newInstance();
                getStore().add(entity);
                currentIndex = getStore().size()-1;
                fireDataSetEvent(new DataSetEvent(DataSetEvent.CREATED));
                return entity;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object createRow(String collectionName) {
        Database db = IOC.queryUtility(Database.class);
        E parentEntity = getStore().get(currentIndex);
        BeanAccess<Collection> beanAccess = new BeanAccess<>(parentEntity, collectionName);
        Collection collection = beanAccess.getValue();
        Class<?> genericReturnType = beanAccess.getGenericReturnType();
        if( db != null ) {
            Object row = getManager().createRow(collectionName);
            collection.add(row);
            fireDataSetEvent(new DataSetEvent(DataSetEvent.ROWS_CREATED));
            fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
            return row;
        } else {
            try {
                Object entity = genericReturnType.newInstance();
                collection.add(entity);
                fireDataSetEvent(new DataSetEvent(DataSetEvent.ROWS_CREATED));
                fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
                return entity;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delete() {
        fireDataSetEvent(new DataSetEvent(DataSetEvent.BEFORE_DELETE));
        Manager<E> manager = getManager();
        if( manager != null ) {
            E entity = getStore().get(currentIndex);
            manager.delete(entity);
            getStore().remove(currentIndex.intValue());
            currentIndex = getStore().size()-1;
        } else {
            getStore().remove(currentIndex.intValue());
            currentIndex = getStore().size()-1;
        }
        fireDataSetEvent(new DataSetEvent(DataSetEvent.DELETED));
    }

    public void deleteRow(String collectionName, E row) {
        Database db = IOC.queryUtility(Database.class);
        E parentEntity = getStore().get(currentIndex);
        BeanAccess<Collection> beanAccess = new BeanAccess<>(parentEntity, collectionName);
        Collection collection = beanAccess.getValue();
        collection.remove(row);
        if( db != null ) {
            getManager().deleteRow(row);
        }
        fireDataSetEvent(new DataSetEvent(DataSetEvent.ROWS_DELETED));
        fireDataSetEvent(new DataSetEvent(DataSetEvent.GET_DIRTY));
    }

    /*
     *  EVENTS
     */
    @Override
    public void addDataSetEventListener(DataSetEventListener listener) {
        dataSetEventListeners.add(listener);
    }

    private void fireDataSetEvent(DataSetEvent event){
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "{0} event fired", event.getEventType().getName());
        for(DataSetEventListener dataSetEventListener: dataSetEventListeners) {
            dataSetEventListener.dataSetEventHandler(event);
        }
        // EntityLister events
        if( listener!= null ){
            E entity = getStore().get(currentIndex);
            if( event.getEventType().equals(DataSetEvent.BEFORE_COMMIT) ){
                listener.beforeCommit(entity);
            }
            // TODO
        }
    }
}
