package com.axiastudio.zoefx.core.controller;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import com.axiastudio.zoefx.core.beans.property.ItemObjectProperty;
import com.axiastudio.zoefx.core.beans.property.ZoeFXProperty;
import com.axiastudio.zoefx.core.events.DataSetEvent;
import com.axiastudio.zoefx.core.events.DataSetEventListener;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.view.*;
import com.axiastudio.zoefx.core.console.ConsoleController;
import com.axiastudio.zoefx.core.view.search.SearchController;
import javafx.beans.*;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class FXController extends BaseController implements DataSetEventListener {

    private Scene scene;
    private DataSet dataset = null;
    private ZSceneMode mode;
    private Behavior behavior = null;
    private FXController me;
    private Map<String, Property> fxProperties = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        me = this;
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void bindDataSet(DataSet dataset){
        this.dataset = dataset;
        Model model = dataset.newModel();
        scanFXProperties();
        initializeChoices();
        initializeColumns();
        setModel(model);
    }


    private void initializeColumns(){
        Model model = dataset.getCurrentModel();
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
                    String lookup=null;
                    if( behavior != null ) {
                        lookup = behavior.getProperties().getProperty(columnId + ".lookup");
                    }
                    if( lookup != null ) {
                        Callback callback = model.getCallback(name, columnId, lookup);
                        column.setCellValueFactory(callback);
                    } else {
                        Callback callback = model.getCallback(name, columnId);
                        column.setCellValueFactory(callback);
                    }
                    //tableView.getItems().addListener(listChangeListener);
                }
            }
        }
    }

    private void initializeChoices(){
        Model model = dataset.getCurrentModel();
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

    private void unsetModel() {
        Model model = dataset.getCurrentModel();
        for( String name: fxProperties.keySet() ){
            Property fxProperty = fxProperties.get(name);
            ZoeFXProperty zoeFXProperty = model.getProperty(name);
            if( fxProperty != null && zoeFXProperty != null ) {
                Bindings.unbindBidirectional(fxProperty, zoeFXProperty);
                fxProperty.removeListener(invalidationListener);
            }
        }
    }

    private void setModel() {
        setModel(dataset.newModel());
    }

    private void setModel(Model model) {
        for( String name: fxProperties.keySet() ){
            Property fxProperty = fxProperties.get(name);
            ZoeFXProperty zoeFXProperty = model.getProperty(name);
            if( zoeFXProperty == null ){
                Node node = scene.lookup("#"+name);
                if( node instanceof TextField ){
                    zoeFXProperty = model.getProperty(name, String.class);
                } else if( node instanceof TextArea ){
                    zoeFXProperty = model.getProperty(name, String.class);
                } else if( node instanceof CheckBox ){
                    zoeFXProperty = model.getProperty(name, Boolean.class);
                } else if( node instanceof ChoiceBox ){
                    zoeFXProperty = model.getProperty(name, Object.class);
                } else if( node instanceof DatePicker ){
                    zoeFXProperty = model.getProperty(name, Date.class);
                } else if( node instanceof TableView ){
                    zoeFXProperty = model.getProperty(name, Collection.class);
                }
            }
            if( fxProperty != null && zoeFXProperty != null ) {
                Bindings.bindBidirectional(fxProperty, zoeFXProperty);
                fxProperty.addListener(invalidationListener);
            }
        }
        //updateCache();
    }

    private void scanFXProperties(){
        Parent root = scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            String name = node.getId();
            Property property = null;
            if( node instanceof TextField ){
                property = ((TextField) node).textProperty();
            } else if( node instanceof TextArea ){
                property = ((TextArea) node).textProperty();
            } else if( node instanceof CheckBox ){
                property = ((CheckBox) node).selectedProperty();
            } else if( node instanceof ChoiceBox ){
                property = ((ChoiceBox) node).valueProperty();
            } else if( node instanceof DatePicker ){
                property = ((DatePicker) node).valueProperty();
            } else if( node instanceof TableView ){
                TableView tableView = (TableView) node;
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tableView.setContextMenu(createContextMenu(tableView));
                property = tableView.itemsProperty();
            }
            if( property != null ){
                fxProperties.put(name, property);
            }
        }
    }

    private void updateCache(){
        for( String name: fxProperties.keySet() ){
            Property property = fxProperties.get(name);
            dataset.putOldValue(property, property.getValue());
        }
    }

    /*
    private void configureModel(Boolean isSet) {
        Model model;
        if( isSet ) {
            model = dataset.newModel();
        } else {
            model = dataset.getCurrentModel();
        }
        Parent root = scene.getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            String name = node.getId();
            Property fxProperty = null;
            Property zoeProperty = null;
            if( node instanceof TextField ){
                fxProperty = fxProperties.get(name); //((TextField) node).textProperty();
                zoeProperty = model.getProperty(name, String.class);
                Validator validator = Validators.getValidator(model.getEntityClass(), name);
                if( validator != null ) {
                    fxProperty.addListener(new TextFieldListener(validator));
                }
            } else if( node instanceof TextArea ){
                fxProperty = fxProperties.get(name); //((TextArea) node).textProperty();
                zoeProperty = model.getProperty(name, String.class);
            } else if( node instanceof CheckBox ){
                fxProperty = fxProperties.get(name); //((CheckBox) node).selectedProperty();
                zoeProperty = model.getProperty(name, Boolean.class);
            } else if( node instanceof ChoiceBox ){
                fxProperty = fxProperties.get(name); //((ChoiceBox) node).valueProperty();
                zoeProperty = model.getProperty(name, Object.class);
            } else if( node instanceof DatePicker ){
                fxProperty = fxProperties.get(name); //((DatePicker) node).valueProperty();
                zoeProperty = model.getProperty(name, Date.class);
            } else if( node instanceof TableView ){
                TableView tableView = (TableView) node;
                tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                tableView.setContextMenu(createContextMenu(tableView));
                fxProperty = fxProperties.get(name); //tableView.itemsProperty();
                zoeProperty = model.getProperty(name, Collection.class);
            }
            if( zoeProperty != null && fxProperty != null) {
                if( isSet ) {
                    Bindings.bindBidirectional(fxProperty, zoeProperty);
                    fxProperty.addListener(invalidationListener);
                } else {
                    Bindings.unbindBidirectional(fxProperty, zoeProperty);
                    //rightProperty.unbind();
                    fxProperty.removeListener(invalidationListener);
                }
                //dataset.putOldValue(fxProperty, fxProperty.getValue());
            }
        }
    }*/

    private ContextMenu createContextMenu(TableView tableView){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem infoItem = new MenuItem("Information");
        infoItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/info.png"))));
        infoItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
                if( selectedItems.size()==0 ) {
                    return;
                }
                List<Object> newStore = new ArrayList<>();
                for( int i=0; i<selectedItems.size(); i++ ) {
                    newStore.add(selectedItems.get(i));
                }
                ZScene newScene = SceneBuilders.queryZScene(newStore, ZSceneMode.DIALOG);
                if( newScene != null ) {
                    Stage newStage = new Stage();
                    newStage.setScene(newScene.getScene());
                    newStage.show();
                }
            }
        });
        MenuItem openItem = new MenuItem("Open");
        openItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/open.png"))));
                openItem.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
                        if (selectedItems.size() == 0) {
                            return;
                        }
                        List<Object> newStore = new ArrayList<>();
                        String referenceProperty = tableView.getId() + ".reference";
                        String reference = behavior.getProperties().getProperty(referenceProperty, null);
                        for (int i = 0; i < selectedItems.size(); i++) {
                            Object item = selectedItems.get(i);
                            if (reference != null) {
                                BeanAccess<Object> ba = new BeanAccess<>(item, reference);
                                newStore.add(ba.getValue());
                            } else {
                                newStore.add(item);
                            }
                        }
                        ZScene newScene = SceneBuilders.queryZScene(newStore, ZSceneMode.DIALOG);
                        if (newScene != null) {
                            Stage newStage = new Stage();
                            newStage.setScene(newScene.getScene());
                            newStage.show();
                        }
                    }
                });
        MenuItem addItem = new MenuItem("Add");
        addItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/add.png"))));
        addItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                String referenceProperty = tableView.getId() + ".reference";
                String reference = behavior.getProperties().getProperty(referenceProperty, null);
                if (reference != null) {
                    System.out.println("Search and select " + referenceProperty);
                    dataset.create(tableView.getId());
                } else {
                    dataset.create(tableView.getId());
                }
                //refreshModel();
                dataset.getDirty();
            }
        });
        MenuItem delItem = new MenuItem("Delete");
        delItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/delete.png"))));
        delItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.out.println("Delete!");
            }
        });

        contextMenu.getItems().addAll(infoItem, openItem, addItem, delItem);
        return contextMenu;
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

    private FXController self(){
        return this;
    }

    public ZSceneMode getMode() {
        return mode;
    }

    public void setMode(ZSceneMode mode) {
        this.mode = mode;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    /*
    public void refresh(){
        unsetModel();
        dataset.goFirst();
        setModel();
        //refreshNavBar();
    }*/

    /*
     *  Navigation Bar
     */

    public EventHandler<ActionEvent> handlerGoFirst = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goFirst();
            setModel(dataset.newModel());
        }
    };
    public EventHandler<ActionEvent> handlerGoPrevious = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goPrevious();
            setModel(dataset.newModel());
        }
    };
    public EventHandler<ActionEvent> handlerGoNext = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goNext();
            setModel(dataset.newModel());
        }
    };
    public EventHandler<ActionEvent> handlerGoLast = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            unsetModel();
            dataset.goLast();
            setModel(dataset.newModel());
        }
    };
    public EventHandler<ActionEvent> handlerSave = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.commit();
        }
    };
    public EventHandler<ActionEvent> handlerConfirm = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            ((Stage) scene.getWindow()).close();
            // TODO: get the parent dirty
        }
    };
    public EventHandler<ActionEvent> handlerCancel = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.revert();
        }
    };
    public EventHandler<ActionEvent> handlerAdd = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.create();
            //refreshModel();
        }
    };
    public EventHandler<ActionEvent> handlerSearch = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            URL url = getClass().getResource("/com/axiastudio/zoefx/core/view/search/search.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = null;
            try {
                root = loader.load(url.openStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            SearchController controller = loader.getController();
            controller.setEntityClass(dataset.getCurrentModel().getEntityClass());
            List<String> columns = new ArrayList<>();
            String searchcolumns = behavior.getProperties().getProperty("searchcolumns");
            if( searchcolumns != null ){
                String[] split = searchcolumns.split(",");
                for( int i=0; i<split.length; i++ ){
                    columns.add(split[i]);
                }
            }
            controller.setColumns(columns);
            controller.setParentController(me);

            Stage stage = new Stage();
            stage.setTitle("Search");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
        }
    };
    public EventHandler<ActionEvent> handlerDelete = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            dataset.delete();
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

    public InvalidationListener invalidationListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            dataset.getDirty();
        }
    };


    @Override
    public void dataSetEventHandler(DataSetEvent event) {
        System.out.println(event.getEventType() + " -> controller");
        if( event.getEventType().equals(DataSetEvent.STORE_CHANGED) ){
            //unsetModel();
            //setModel();
        } else if( event.getEventType().equals(DataSetEvent.REVERT) ){
            //unsetModel();
            //setModel();
            //updateCache();
        } else if( event.getEventType().equals(DataSetEvent.INDEX_CHANGED) ){
            //updateCache();
        } else if( event.getEventType().equals(DataSetEvent.CREATE) ){
            //refreshModel();
        } else if( event.getEventType().equals(DataSetEvent.COMMIT) ){
            //updateCache();
        }
    }
}