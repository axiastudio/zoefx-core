package com.axiastudio.zoefx.controller;

import com.axiastudio.zoefx.db.ItemEnumProperty;
import com.axiastudio.zoefx.db.Model;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
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
        setModel();
    }

    private void initializeChoices(){
        Model model = context.getModel();
        Parent root = this.scene.getRoot();
        AnchorPane pane = (AnchorPane) root;
        for( Node node: pane.getChildren() ){
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
        Model model = context.getModel();
        Parent root = this.scene.getRoot();
        AnchorPane pane = (AnchorPane) root;
        for( Node node: pane.getChildren() ){
            String name = node.getId();
            Property rightProperty = model.getProperty(name);
            Property leftProperty = null;
            if( node instanceof TextField){
                leftProperty = ((TextField) node).textProperty();
            } else if( node instanceof TextArea){
                leftProperty = ((TextArea) node).textProperty();
            } else if( node instanceof CheckBox){
                leftProperty = ((CheckBox) node).selectedProperty();
            } else if( node instanceof ChoiceBox){
                leftProperty = ((ChoiceBox) node).valueProperty();
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


    public DataContext getContext() {
        return context;
    }


    private void refreshNavBar(){
        AnchorPane pane = (AnchorPane) this.scene.getRoot();
        Node lookup = pane.lookup("#navigationBar");
        ((ZoeToolBar) lookup).refresh();
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
            /*
            for( Property property: model.getChanges() ){
                String fieldName = model.getFieldNameFromProperty(property) // I need this name
                String capitalizedName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1)
                model.getParent()."set${capitalizedName}"(property.getValue())
            }
            model.clearChanges()
            dirty = false
            refreshNavBar()*/
        }
    };
    public EventHandler<ActionEvent> handlerCancel = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            List<Property> changes = context.getChanges();
            for( Property property: changes ){
                System.out.println(property);
                //String fieldName = model.getFieldNameFromProperty(property) // I need this name
                //String capitalizedName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1)
                //property.setValue(model.getParent()."get${capitalizedName}"())
            }
            context.clearChanges();

            refreshNavBar();
        }
    };
    public EventHandler<ActionEvent> handlerConsole = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            /*
            try {
                URL url = getClass().getResource("/com/axiastudio/zoe/view/ui/console.fxml")
                FXMLLoader loader = new FXMLLoader()
                loader.setLocation(url)
                loader.setBuilderFactory(new JavaFXBuilderFactory())
                Parent root = (Parent) loader.load(url.openStream())

                Console console = (Console) loader.getController()
                console.setController(self())

                Stage stage = new Stage()
                stage.setTitle("Zoe Groovy Console")
                stage.setScene(new Scene(root, 450, 450))
                stage.show()

            } catch (IOException ex) {
                ex.printStackTrace()
            }*/
        }
    };


    /*
     *  Listeners
     */

    public ChangeListener changeListener = new ChangeListener() {
        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            context.addChange((Property) observable);
            context.getDirty();
            refreshNavBar();
        }
    };


}