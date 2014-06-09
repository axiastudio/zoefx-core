package com.axiastudio.zoefx.core.view.search;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.beans.BeanClassAccess;
import com.axiastudio.zoefx.core.beans.property.CallbackBuilder;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.db.Database;
import com.axiastudio.zoefx.core.db.Manager;
import com.axiastudio.zoefx.core.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

/**
 * User: tiziano
 * Date: 21/05/14
 * Time: 15:21
 */
public class SearchController<T> implements Initializable {

    @FXML
    private TableView results;

    private Class entityClass;
    private Callback<List<T>, Boolean> callback=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        results.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setEntityClass(Class<? extends T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setCallback(Callback<List<T>, Boolean> callback) {
        this.callback = callback;
    }

    public void setColumns(List<String> columns) {
        for( String property: columns ) {
            TableColumn column = new TableColumn();
            Callback callback = CallbackBuilder.create().beanClass(entityClass).field(property).build(); /// XXX
            column.setCellValueFactory(callback);
            // custom date order
            BeanClassAccess beanClassAccess = new BeanClassAccess(entityClass, property);
            if( Date.class.isAssignableFrom(beanClassAccess.getReturnType()) ) {
                column.setComparator(Comparator.nullsFirst(Comparators.DateComparator));
            }
            results.getColumns().add(column);
        }
    }

    @FXML
    private void search(ActionEvent event){
        Database db = Utilities.queryUtility(Database.class);
        Manager<T> manager = db.createManager(entityClass);
        List<T> all = manager.getAll();
        DataSet<T> dataSet = new DataSet<>(all);
        ObservableList<T> observableList = FXCollections.observableArrayList(all);
        results.setItems(observableList);
    }

    @FXML
    private void apply(ActionEvent event){
        ObservableList<T> items = results.getSelectionModel().getSelectedItems();
        callback.call(items);
    }
}
