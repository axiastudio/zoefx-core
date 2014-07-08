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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @FXML
    private VBox filterbox;

    private Class entityClass;
    private Callback<List<T>, Boolean> callback=null;
    private List<String> criteria = new ArrayList<>();

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

    public void setCriteria(List<String> criteria){
        for( String property: criteria ){
            HBox hBox = new HBox();
            Label label = new Label(property);
            label.setMinWidth(100.0);
            Node node=null;
            BeanClassAccess beanClassAccess = new BeanClassAccess(entityClass, property);
            Class<?> returnType = beanClassAccess.getReturnType();
            if( String.class.isAssignableFrom(beanClassAccess.getReturnType()) ) {
                TextField textField = new TextField();
                textField.setMinWidth(200.0);
                textField.setId(property);
                node = textField;
            } else if( Object.class.isAssignableFrom(beanClassAccess.getReturnType()) ) {
                List superset = new ArrayList();
                if( returnType.isEnum() ) {
                    for (Object obj : returnType.getEnumConstants() ) {
                        superset.add(obj);
                    }

                } else {
                    Database database = Utilities.queryUtility(Database.class);
                    if( database != null ) {
                        Manager<?> manager = database.createManager(returnType);
                        for (Object obj : manager.getAll()) {
                            superset.add(obj);
                        }
                    }
                }
                ChoiceBox choiceBox = new ChoiceBox();
                choiceBox.setId(property);
                ObservableList choices = FXCollections.observableArrayList(superset);
                choiceBox.setItems(choices);
                node = choiceBox;
            }
            if( node != null ) {
                hBox.getChildren().addAll(label, node);
                filterbox.getChildren().add(hBox);
            }
        }
    }

    @FXML
    private void search(ActionEvent event){
        Database db = Utilities.queryUtility(Database.class);
        Manager<T> manager = db.createManager(entityClass);
        Map<String, Object> map = new HashMap<>();
        for( Node node: filterbox.getChildren() ){
            HBox hBox = (HBox) node;
            Node criteriaNode = hBox.getChildren().get(1);
            if( criteriaNode instanceof TextField ){
                TextField criteriaField = (TextField) criteriaNode;
                String fieldName = criteriaField.getId();
                String value = criteriaField.getText();
                if( value.length()>0 ) {
                    map.put(fieldName, value);
                }
            } else if( criteriaNode instanceof ChoiceBox ){
                ChoiceBox choiceBox = (ChoiceBox) criteriaNode;
                String fieldName = choiceBox.getId();
                Object value = choiceBox.getSelectionModel().getSelectedItem();
                map.put(fieldName, value);
            }
        }
        List<T> store;
        if( map.keySet().size()>0 ){
            store = manager.query(map);
        } else {
            store = manager.getAll();
        }
        if( store.size()>0 ) {
            DataSet<T> dataSet = new DataSet<>(store);
            ObservableList<T> observableList = FXCollections.observableArrayList(store);
            results.setItems(observableList);
        }
    }

    @FXML
    private void apply(ActionEvent event){
        ObservableList<T> items = results.getSelectionModel().getSelectedItems();
        callback.call(items);
    }
}
