package com.axiastudio.zoefx.core.controller;

import com.axiastudio.zoefx.core.beans.BeanAccess;
import com.axiastudio.zoefx.core.beans.BeanClassAccess;
import com.axiastudio.zoefx.core.beans.LookupStringConverter;
import com.axiastudio.zoefx.core.beans.property.ItemObjectProperty;
import com.axiastudio.zoefx.core.beans.property.ZoeFXProperty;
import com.axiastudio.zoefx.core.db.TimeMachine;
import com.axiastudio.zoefx.core.events.DataSetEvent;
import com.axiastudio.zoefx.core.events.DataSetEventListener;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.skins.Skins;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:04
 */
public class FXController extends BaseController implements DataSetEventListener {

    private DataSet dataset = null;
    private ZSceneMode mode;
    private Behavior behavior = null;
    private TimeMachine timeMachine = null;
    private Map<String, Property> fxProperties = new HashMap<>();
    private Map<String,TableView> tableViews;
    private ResourceBundle resourceBundle;


    @Override
    public void initialize(URL url, ResourceBundle resource) {
        resourceBundle = resource;
    }

    public void bindDataSet(DataSet dataset){
        this.dataset = dataset;
        if( dataset.size()== 0 ){
            dataset.create();
        }
        Model model = dataset.newModel();
        scanFXProperties();
        initializeChoices();
        initializeColumns();
        // first show
        setModel(model);
        timeMachine.createSnapshot(fxProperties.values());
    }


    private void initializeColumns(){
        Model model = dataset.getCurrentModel();
        Parent root = getScene().getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        tableViews = new HashMap<>();
        for( Node node: nodes ){
            if( node instanceof TableView){
                TableView tableView = (TableView) node;
                tableViews.put(node.getId(), tableView);
                ObservableList<TableColumn> columns = tableView.getColumns();
                for( TableColumn column: columns ){
                    String name = node.getId();
                    String columnId = column.getId();
                    if( columnId == null ) {
                        columnId = column.getText().toLowerCase(); // XXX: RT-36633 JavaXFX issue
                        // https://javafx-jira.kenai.com/browse/RT-36633
                        // solved in java 8u20
                    }
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
                    // custom date order
                    BeanClassAccess beanClassAccess = new BeanClassAccess(model.getEntityClass(), columnId);
                    if( beanClassAccess.getReturnType() != null && Date.class.isAssignableFrom(beanClassAccess.getReturnType()) ) {
                        column.setComparator(Comparator.nullsFirst(Comparators.DateComparator));
                    }
                    //tableView.getItems().addListener(listChangeListener);
                }
            }
        }
    }

    private void initializeChoices(){
        Model model = dataset.getCurrentModel();
        Parent root = getScene().getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<Node>());
        for( Node node: nodes ){
            if( node instanceof ChoiceBox){
                String name = node.getId();
                Property property = model.getProperty(name, Object.class);
                List superset = ((ItemObjectProperty) property).getSuperset();
                ObservableList choices = FXCollections.observableArrayList(superset);
                ChoiceBox choiceBox = (ChoiceBox) node;
                choiceBox.setItems(choices);
                if( behavior != null ) {
                    String lookup = behavior.getProperties().getProperty(name + ".lookup");
                    if (lookup != null) {
                        choiceBox.setConverter(new LookupStringConverter<>(lookup));
                    }
                }
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
                Node node = getScene().lookup("#"+name);
                if( node instanceof TextField ){
                    zoeFXProperty = model.getProperty(name, String.class);
                } else if( node instanceof TextArea ){
                    zoeFXProperty = model.getProperty(name, String.class);
                } else if( node instanceof Label ){
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
        Parent root = getScene().getRoot();
        Pane container = (Pane) root;
        List<Node> nodes = findNodes(container, new ArrayList<>());
        for( Node node: nodes ){
            String name = node.getId();
            Property property = null;
            if( node instanceof TextField ){
                property = ((TextField) node).textProperty();
            } else if( node instanceof TextArea ){
                property = ((TextArea) node).textProperty();
            } else if( node instanceof Label ){
                property = ((Label) node).textProperty();
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
            /*
            Validator validator = Validators.getValidator(dataset.getCurrentModel().getEntityClass(), name);
            if( validator != null ) {
                property.addListener(new TextFieldListener(validator));
            }
            */
        }
    }


    private ContextMenu createContextMenu(TableView tableView){
        ContextMenu contextMenu = new ContextMenu();

        MenuItem infoItem = new MenuItem("Information");
        String resourcesFolder = Skins.getActiveSkin().resourcesFolder();
        if( resourcesFolder!=null ) {
            infoItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(resourcesFolder + "info.png"))));
        } else {
            infoItem.setText(resourceBundle.getString("toolbar.info_short"));
        }
        infoItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
                if( selectedItems.size()==0 ) {
                    return;
                }
                List newStore = new ArrayList<>();
                for( int i=0; i<selectedItems.size(); i++ ) {
                    newStore.add(selectedItems.get(i));
                }
                ZSceneBuilder sceneBuilder = SceneBuilders.querySceneBuilder(newStore.get(0).getClass());
                FXController controller = Controllers.queryController(sceneBuilder);
                ZScene newScene = sceneBuilder.manager(getDataset().getManager()).store(newStore).controller(controller)
                        .mode(ZSceneMode.DIALOG).build();
                if( newScene != null ) {
                    Stage newStage = new Stage();
                    newStage.setScene(newScene.getScene());
                    newStage.show();
                    newStage.requestFocus();
                }
                dataset.getDirty(); /// XXX: to implement a callback?
            }
        });
        MenuItem openItem = new MenuItem("Open");
        if( resourcesFolder!=null ) {
            openItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(resourcesFolder + "open.png"))));
        } else {
            openItem.setText(resourceBundle.getString("toolbar.open_short"));
        }
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ObservableList selectedItems = tableView.getSelectionModel().getSelectedItems();
                if (selectedItems.size() == 0) {
                    return;
                }
                List newStore = new ArrayList<>();
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
                ZSceneBuilder sceneBuilder = SceneBuilders.querySceneBuilder(newStore.get(0).getClass());
                FXController controller = Controllers.queryController(sceneBuilder);
                ZScene newScene = sceneBuilder.manager(getDataset().getManager()).store(newStore).controller(controller)
                        .mode(ZSceneMode.WINDOW).build();
                if (newScene != null) {
                    Stage newStage = new Stage();
                    newStage.setScene(newScene.getScene());
                    newStage.show();
                    newStage.requestFocus();
                }
                dataset.getDirty(); /// XXX: to implement a callback?
            }
        });
        MenuItem addItem = new MenuItem("Add");
        if( resourcesFolder!=null ) {
            addItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(resourcesFolder + "add.png"))));
        } else {
            addItem.setText(resourceBundle.getString("toolbar.add_short"));
        }
        addItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                final String collectionName = tableView.getId();
                String referenceProperty = collectionName + ".reference";
                String searchcolumnsProperty = collectionName + ".searchcolumns";
                String referenceName = behavior.getProperties().getProperty(referenceProperty, null);
                String searchcolumns = behavior.getProperties().getProperty(searchcolumnsProperty, "caption"); // XXX: default caption?
                if (referenceName != null) {
                    Class classToSearch = null;
                    try {
                        Class parentEntityClass = dataset.getCurrentModel().getEntityClass();
                        Class<?> collectionGenericReturnType = (new BeanClassAccess(parentEntityClass, collectionName)).getGenericReturnType();
                        Class<?> referenceReturnType = (new BeanClassAccess(collectionGenericReturnType, referenceName)).getReturnType();
                        String className = referenceReturnType.getName();
                        classToSearch = Class.forName(className);
                        Callback callback = new Callback<List, Boolean>() {
                            @Override
                            public Boolean call(List items) {
                                for( Object item: items ){
                                    Object entity = dataset.createRow(collectionName);
                                    BeanAccess<Object> ba = new BeanAccess<>(entity, referenceName);
                                    ba.setValue(item);
                                    refresh();
                                }
                                return true;
                            }
                        };
                        Stage stage = searchStage(classToSearch, searchcolumns, callback);
                        stage.show();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Object entity = dataset.createRow(collectionName);
                    List newStore = new ArrayList<>();
                    newStore.add(entity);
                    ZSceneBuilder sceneBuilder = SceneBuilders.querySceneBuilder(newStore.get(0).getClass());
                    FXController controller = Controllers.queryController(sceneBuilder);
                    ZScene newScene = sceneBuilder.manager(getDataset().getManager()).store(newStore).controller(controller)
                            .mode(ZSceneMode.DIALOG).build();
                    if (newScene != null) {
                        Stage newStage = new Stage();
                        newStage.setScene(newScene.getScene());
                        newStage.show();
                        newStage.requestFocus();
                    }
                }
                //initializeColumns();
                dataset.getDirty(); /// XXX: to implement a callback?
            }
        });
        MenuItem delItem = new MenuItem("Delete");
        if( resourcesFolder!=null ) {
            delItem.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(resourcesFolder + "delete.png"))));
        } else {
            delItem.setText(resourceBundle.getString("toolbar.delete_short"));
        }
        delItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                final String collectionName = tableView.getId();
                List selectedItems =  new ArrayList (tableView.getSelectionModel().getSelectedItems());
                for( Object item: selectedItems ) {
                    dataset.deleteRow(collectionName, item);
                }
                dataset.getDirty(); /// XXX: to implement a callback?
            }
        });

        // accesso policy
        addItem.disableProperty().bind(dataset.canUpdateProperty().not());
        delItem.disableProperty().bind(dataset.canUpdateProperty().not());
        infoItem.disableProperty().bind(dataset.canUpdateProperty().not());
        openItem.disableProperty().bind(dataset.canUpdateProperty().not());

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
    public Model getCurrentModel() {
        return dataset.getCurrentModel();
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

    public void setTimeMachine(TimeMachine timeMachine) {
        this.timeMachine = timeMachine;
    }

    public void refresh(){
        unsetModel();
        Model model = dataset.newModel();
        setModel(model);
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
        for( TableView tableView: tableViews.values() ){
            // XXX: workaround for https://javafx-jira.kenai.com/browse/RT-22599
            ObservableList items = tableView.getItems();
            tableView.itemsProperty().removeListener(invalidationListener);
            tableView.setItems(null);
            tableView.layout();
            tableView.setItems(items);
            tableView.itemsProperty().addListener(invalidationListener);
        }
    }

    private Stage searchStage(Class classToSearch, String searchcolumns, Callback callback) {
        return searchStage(classToSearch, searchcolumns, callback, null);
    }

    private Stage searchStage(Class classToSearch, String searchcolumns, Callback callback, String searchcriteria) {
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

        // search columns
        SearchController controller = loader.getController();
        controller.setEntityClass(classToSearch);
        controller.setBehavior(behavior);
        List<String> columns = new ArrayList<>();
        if( searchcolumns != null ){
            String[] split = searchcolumns.split(",");
            for( int i=0; i<split.length; i++ ){
                columns.add(split[i]);
            }
        }
        controller.setColumns(columns);

        // search criteria
        List<String> criteria = new ArrayList<>();
        if( searchcriteria != null ){
            String[] split = searchcriteria.split(",");
            for( int i=0; i<split.length; i++ ){
                criteria.add(split[i]);
            }
        }
        controller.setCriteria(criteria);

        // callback
        controller.setCallback(callback);

        //controller.setParentDataSet(dataset);

        Stage stage = new Stage();
        stage.setTitle("Search");
        stage.setScene(new Scene(root, 450, 450));
        return stage;
    }

    /*
     *  Navigation Bar
     */

    public EventHandler<ActionEvent> handlerGoFirst = e -> {
        unsetModel();
        dataset.goFirst();
        setModel(dataset.newModel());
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerGoPrevious = e -> {
        unsetModel();
        dataset.goPrevious();
        setModel(dataset.newModel());
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerGoNext = e -> {
        unsetModel();
        dataset.goNext();
        setModel(dataset.newModel());
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerGoLast = e -> {
        unsetModel();
        dataset.goLast();
        setModel(dataset.newModel());
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerSave = e -> {
        dataset.commit();
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerConfirm = e -> ((Stage) getScene().getWindow()).close();
    public EventHandler<ActionEvent> handlerCancel = e -> {
        timeMachine.rollback();
        dataset.revert();
        timeMachine.resetAndcreateSnapshot(fxProperties.values());
    };
    public EventHandler<ActionEvent> handlerAdd = e -> {
        dataset.create();
        unsetModel();
        setModel(dataset.newModel());
    };
    public EventHandler<ActionEvent> handlerSearch = e -> {
        Class classToSearch = dataset.getCurrentModel().getEntityClass();
        String searchcolumns = behavior.getProperties().getProperty("searchcolumns");
        String searchcriteria = behavior.getProperties().getProperty("searchcriteria");

        Callback callback = new Callback<List, Boolean>() {
            @Override
            public Boolean call(List items) {
                List store = new ArrayList();
                for( Object item: items ){
                    store.add(item);
                }
                dataset.setStore(store);
                return true;
            }
        };
        Stage stage = searchStage(classToSearch, searchcolumns, callback, searchcriteria);
        stage.show();
    };
    public EventHandler<ActionEvent> handlerDelete = e -> {
        dataset.delete();
        unsetModel();
        setModel(dataset.newModel());
    };
    public EventHandler<ActionEvent> handlerRefresh = e -> refresh();
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
    public EventHandler<ActionEvent> handlerInfo = e -> {
        //System.out.println("Info");
    };


    /*
     *  Listeners
     */

    public InvalidationListener invalidationListener = observable -> dataset.getDirty();


    @Override
    public void dataSetEventHandler(DataSetEvent event) {
        //System.out.println(event.getEventType());
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "{0} event handled", event.getEventType().getName());
        if( event.getEventType().equals(DataSetEvent.STORE_CHANGED) ){
            storeChanged();
            unsetModel();
            setModel();
        } else if( event.getEventType().equals(DataSetEvent.ROWS_CREATED) ){
            rowsCreated();
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.ROWS_DETETED) ){
            rowsDeleted();
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.GET_DIRTY) ){
            getDirty();
        } else if( event.getEventType().equals(DataSetEvent.BEFORE_COMMIT) ){
            beforeCommit();
        } else if( event.getEventType().equals(DataSetEvent.COMMITED) ){
            committed();
        } else if( event.getEventType().equals(DataSetEvent.INDEX_CHANGED) ){
            indexChanged();
        }
    }

    /*
     * Methods to implement in a custom controller.
     */
    protected void storeChanged(){}
    protected void rowsCreated(){}
    protected void rowsDeleted(){}
    protected void getDirty(){}
    protected void beforeCommit(){}
    protected void committed(){}
    protected void indexChanged(){}
}