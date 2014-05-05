package com.axiastudio.zoefx.core.controller;

import com.axiastudio.zoefx.core.beans.property.ItemObjectProperty;
import com.axiastudio.zoefx.core.events.ModelEvent;
import com.axiastudio.zoefx.core.listeners.TextFieldListener;
import com.axiastudio.zoefx.core.validators.Validator;
import com.axiastudio.zoefx.core.validators.Validators;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.view.Model;
import com.axiastudio.zoefx.core.console.ConsoleController;
import com.axiastudio.zoefx.core.view.ZToolBar;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:04
 */
public class FXController extends BaseController {

    private Scene scene;
    private DataSet dataset =null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void bindDataSet(DataSet dataset){
        this.dataset = dataset;
        initializeChoices();
        initializeColumns();
        setModel();
        refreshNavBar();
    }


    private void initializeColumns(){
        Model model = dataset.newModel();
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            if( node instanceof TableView){
                TableView tableView = (TableView) node;
                ObservableList<TableColumn> columns = tableView.getColumns();
                for( TableColumn column: columns ){
                    String name = node.getId();
                    //String columnId = column.getId();
                    String columnId = column.getText().toLowerCase(); // XXX: RT-36633 JavaXFX issue
                    // https://javafx-jira.kenai.com/browse/RT-36633
                    Callback callback = model.getCallback(name, columnId);
                    column.setCellValueFactory(callback);
                }
            }
        }
    }

    private void initializeChoices(){
        Model model = dataset.newModel();
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            if( node instanceof ChoiceBox){
                String name = node.getId();
                Property property = model.getProperty(name, Object.class);
                List superset = ((ItemObjectProperty) property).getSuperset();
                ObservableList choices = FXCollections.observableArrayList(superset);
                ((ChoiceBox) node).setItems(choices);
            }
        }
    }

    private void refreshModel() {
        unsetModel();
        setModel();
        refreshNavBar();
    }

    private void unsetModel() {
        configureModel(false);
    }

    private void setModel() {
        configureModel(true);
    }

    private void configureModel(Boolean isSet) {
        Model model;
        if( isSet ) {
            model = dataset.newModel();
            scene.getRoot().fireEvent(new ModelEvent(ModelEvent.REFRESH));
        } else {
            model = dataset.getCurrentModel();
        }
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            String name = node.getId();
            Property leftProperty = null;
            Property rightProperty = null;
            if( node instanceof TextField ){
                leftProperty = ((TextField) node).textProperty();
                rightProperty = model.getProperty(name, String.class);
                Validator validator = Validators.getValidator(model.getEntityClass(), name);
                if( validator != null ) {
                    leftProperty.addListener(new TextFieldListener(validator));
                }
            } else if( node instanceof TextArea ){
                leftProperty = ((TextArea) node).textProperty();
                rightProperty = model.getProperty(name, String.class);
            } else if( node instanceof CheckBox ){
                leftProperty = ((CheckBox) node).selectedProperty();
                rightProperty = model.getProperty(name, Boolean.class);
            } else if( node instanceof ChoiceBox ){
                leftProperty = ((ChoiceBox) node).valueProperty();
                rightProperty = model.getProperty(name, Object.class);
            } else if( node instanceof DatePicker ){
                leftProperty = ((DatePicker) node).valueProperty();
                rightProperty = model.getProperty(name, Date.class);
            } else if( node instanceof TableView ){
                TableView tableView = (TableView) node;
                leftProperty = tableView.itemsProperty();
                rightProperty = model.getProperty(name, Collection.class);
            }
            if( rightProperty != null && leftProperty != null) {
                if( isSet ) {
                    Bindings.bindBidirectional(leftProperty, rightProperty);
                    leftProperty.addListener(changeListener);
                } else {
                    Bindings.unbindBidirectional(leftProperty, rightProperty);
                    leftProperty.removeListener(changeListener);
                }
            }
        }
    }

    private List<Node> findNodes( Pane container, List<Node> nodes ){
        for( Node node: container.getChildren() ){
            if( node instanceof Pane ){
                nodes = findNodes((Pane) node, nodes);
            } else if( node instanceof TabPane ){
                for( Tab tab: ((TabPane) node).getTabs() ) {
                    nodes = findNodes((Pane) tab.getContent(), nodes);
                }
            }
            else if( node.getId() != null && node.getId() != "" ){
                nodes.add(node);
            }
        }
        return nodes;
    }


    public DataSet getDataset() {
        return dataset;
    }


    private void refreshNavBar(){
        Pane pane = (Pane) this.scene.getRoot();
        Node lookup = pane.lookup("#navigationBar");
        ((ZToolBar) lookup).refresh();
    }

    private FXController self(){
        return this;
    }


    /*
     *  Navigation Bar
     */

    public EventHandler<ActionEvent> handlerGoFirst = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goFirst();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoPrevious = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goPrevious();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoNext = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goNext();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoLast = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goLast();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerSave = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.commit();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerCancel = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.revert();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerAdd = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.create();
            refreshModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerDelete = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.delete();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerConsole = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            URL url = getClass().getResource("/com/axiastudio/zoefx/core/console/console.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = null;
            try {
                root = loader.load(url.openStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            ConsoleController console = loader.getController();
            console.setController(self());

            Stage stage = new Stage();
            stage.setTitle("Zoe FX Script Console");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        }
    };


    /*
     *  Listeners
     */

    public ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            dataset.addChange((Property) observable, oldValue, newValue);
            refreshNavBar();
        }
    };


}