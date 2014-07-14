package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.BaseController;
import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.db.DataSetBuilder;
import com.axiastudio.zoefx.core.db.Manager;
import com.axiastudio.zoefx.core.db.TimeMachine;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 22:52
 */
public class ZSceneBuilder<E> {

    private Manager<E> manager=null;
    private List<E> store=null;
    private Class entityClass=null;
    private URL url;
    private String title;
    private BaseController controller=null;
    private InputStream propertiesStrem=null;
    private Integer width=500;
    private Integer height=375;
    private ZSceneMode mode=ZSceneMode.WINDOW;

    public ZSceneBuilder() {
    }

    public static ZSceneBuilder create() {
        return new ZSceneBuilder();
    }

    public static ZSceneBuilder create(Class<?> entityClass) {
        ZSceneBuilder builder = new ZSceneBuilder();
        builder.setEntityClass(entityClass);
        return builder;
    }

    public ZSceneBuilder properties(InputStream stream){
        propertiesStrem = stream;
        return this;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public ZSceneBuilder manager(Manager manager){
        this.manager = manager;
        return this;
    }

    public ZSceneBuilder store(List<E> store) {
        this.store = store;
        return this;
    }

    public ZSceneBuilder title(String title){
        this.title = title;
        return this;
    }

    public String getTitle() {
        if( title == null ) {
            return url.getFile().substring(url.getFile().lastIndexOf("/")+1,
                    url.getFile().lastIndexOf("."));
        }
        return title;
    }

    public ZSceneBuilder url(URL url){
        this.url = url;
        return this;
    }

    public ZSceneBuilder width(Integer width){
        this.width = width;
        return this;
    }

    public ZSceneBuilder height(Integer height){
        this.height = height;
        return this;
    }

    public ZSceneBuilder controller(BaseController controller){
        this.controller = controller;
        return this;
    }

    public ZSceneBuilder mode(ZSceneMode mode){
        this.mode = mode;
        return this;
    }

    public ZScene build(){
        ResourceBundle bundle = ResourceBundle.getBundle("com.axiastudio.zoefx.core.resources.i18n");
        FXMLLoader loader = new FXMLLoader(url, bundle);
        loader.setResources(bundle);
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        if( controller != null ) {
            loader.setController(controller);
        }
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, width, height);
        ZScene zScene = new ZScene();
        zScene.setScene(scene);
        if( controller instanceof FXController ) {
            FXController fxController = (FXController) controller;
            fxController.setMode(mode);
            ZToolBar toolBar = new ZToolBar(bundle);
            if( root instanceof VBox ){
                if( ((VBox) root).getChildren().get(0) instanceof MenuBar) {
                    ((VBox) root).getChildren().add(1, toolBar);
                } else {
                    ((VBox) root).getChildren().add(0, toolBar);
                }
            } else {
                ((Pane) root).getChildren().add(toolBar);
            }

            toolBar.setController(fxController);
            fxController.setScene(scene);
            if( propertiesStrem != null ){
                Behavior behavior = new Behavior(propertiesStrem);
                fxController.setBehavior(behavior);
            }
            TimeMachine timeMachine = new TimeMachine();
            fxController.setTimeMachine(timeMachine);
            if( store == null && manager != null ) {
                store = manager.getAll();
            }
            DataSet<E> dataset =  DataSetBuilder.create(entityClass).store(store).manager(manager).build();
            dataset.addDataSetEventListener(toolBar);
            dataset.addDataSetEventListener(fxController);
            fxController.bindDataSet(dataset);

            toolBar.canSelectProperty().bind(dataset.canSelectProperty());
            toolBar.canInsertProperty().bind(dataset.canInsertProperty());
            toolBar.canUpdateProperty().bind(dataset.canUpdateProperty());
            toolBar.canDeleteProperty().bind(dataset.canDeleteProperty());
        }
        return zScene;
    }

}
