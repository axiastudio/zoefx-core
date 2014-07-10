package com.axiastudio.zoefx.core.db;

import java.util.List;

/**
 * User: tiziano
 * Date: 10/07/14
 * Time: 09:30
 */
public class DataSetBuilder<E> {

    private List<E> store;
    private Manager<E> manager;
    private Class<E> entityClass;

    public DataSetBuilder() {
    }

    public static <E> DataSetBuilder<E> create(Class<E> klass) {
        DataSetBuilder builder = new DataSetBuilder();
        builder.entityClass = klass;
        return builder;
    }

    public DataSetBuilder store(List<E> store){
        this.store = store;
        return this;
    }

    public DataSetBuilder manager(Manager<E> manager){
        this.manager = manager;
        return this;
    }

    public DataSet build(){
        DataSet dataSet = new DataSet();
        dataSet.setStore(store);
        dataSet.setEntityClass(entityClass);
        dataSet.setManager(manager);
        return dataSet;
    }
}
