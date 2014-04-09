package com.axiastudio.zoefx.controller;

import com.axiastudio.zoefx.db.ItemEnumProperty;
import com.axiastudio.zoefx.db.Model;
import com.axiastudio.zoefx.scriptengine.console.ConsoleController;
import com.axiastudio.zoefx.view.DataContext;
import com.axiastudio.zoefx.view.ZoeToolBar;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:04
 */
public class FXController implements Initializable {

    private Scene scene;
    private DataContext context=null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void bindDataContext(DataContext context){
        this.context = context;
        initializeChoices();
        initializeColumns();
        setModel();
    }


    private void initializeColumns(){
        Model model = context.newModel();
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            if( node instanceof TableView){
                TableView tableView = (TableView) node;
                ObservableList<TableColumn> columns = tableView.getColumns();
                for( TableColumn column: columns ){
                    String name = node.getId();
                    // String columnId = column.getId();
                    String columnId = column.getText().toLowerCase(); // XXX: wrong!
                    PropertyValueFactory propertyValueFactory = model.getPropertyValueFactory(name, columnId);
                    column.setCellValueFactory(propertyValueFactory);
                }
            }
        }
    }

    private void initializeChoices(){
        Model model = context.newModel();
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            if( node instanceof ChoiceBox){
                String name = node.getId();
                Property property = model.getProperty(name);
                List<Enum> enumConstants = ((ItemEnumProperty) property).getEnumConstants();
                ObservableList<Enum> choices = FXCollections.observableArrayList(enumConstants);
                ((ChoiceBox) node).setItems(choices);
            }
        }
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
            model = context.newModel();
        } else {
            model = context.getCurrentModel();
        }
        Parent root = this.scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            String name = node.getId();
            Property rightProperty = model.getProperty(name);
            if( rightProperty == null ){
                continue;
            }
            Property leftProperty = null;
            if( node instanceof TextField ){
                leftProperty = ((TextField) node).textProperty();
            } else if( node instanceof TextArea ){
                leftProperty = ((TextArea) node).textProperty();
            } else if( node instanceof CheckBox ){
                leftProperty = ((CheckBox) node).selectedProperty();
            } else if( node instanceof ChoiceBox ){
                leftProperty = ((ChoiceBox) node).valueProperty();
            } else if( node instanceof DatePicker ){
                leftProperty = ((DatePicker) node).valueProperty();
            } else if( node instanceof TableView ){
                TableView tableView = (TableView) node;
                leftProperty = tableView.itemsProperty();
                /*
                ObservableList columns = tableView.getColumns();
                for( Object obj: columns){
                    TableColumn column = (TableColumn) obj;
                    String columnId = column.getId();
                    if( columnId != null ) {
                        column.setCellValueFactory(model.getPropertyValueFactory(name, columnId));
                    }
                }*/
            }
            if( leftProperty != null ) {
                if( isSet ) {
                    Bindings.bindBidirectional(leftProperty, rightProperty);
                    leftProperty.addListener(this.changeListener);
                } else {
                    Bindings.unbindBidirectional(leftProperty, rightProperty);
                    leftProperty.removeListener(this.changeListener);
                }
            }
        }
    }

    private List<Node> findNodes( Pane container, List<Node> nodes ){
        for( Node node: container.getChildren() ){
            if( node instanceof Pane ){
                nodes = findNodes((Pane) node, nodes);
            } else if( node.getId() != null && node.getId() != "" ){
                nodes.add(node);
            }
        }
        return nodes;
    }


    public DataContext getContext() {
        return context;
    }


    private void refreshNavBar(){
        Pane pane = (Pane) this.scene.getRoot();
        Node lookup = pane.lookup("#navigationBar");
        ((ZoeToolBar) lookup).refresh();
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
            context.goFirst();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoPrevious = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            context.goPrevious();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoNext = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            context.goNext();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerGoLast = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            context.goLast();
            setModel();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerSave = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            context.commit();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerCancel = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            context.revert();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerAdd = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            context.create();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerDelete = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            context.delete();
            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerConsole = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            URL url = getClass().getResource("/com/axiastudio/zoefx/scriptengine/console/console.fxml");
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
            stage.setTitle("Zoe FX Groovy Console");
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
            context.addChange((Property) observable, oldValue, newValue);
            refreshNavBar();
        }
    };


}