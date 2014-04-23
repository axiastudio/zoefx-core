package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.FXController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 22:52
 */
public class ZoeSceneBuilder {

    private DataContext context;
    private URL url;
    private FXController controller=null;

    public ZoeSceneBuilder() {
    }

    public static ZoeSceneBuilder create() {
        return new ZoeSceneBuilder();
    }

    public ZoeSceneBuilder datacontext(DataContext context){
        this.context = context;
        return this;
    }

    public ZoeSceneBuilder url(URL url){
        this.url = url;
        return this;
    }

    public ZoeSceneBuilder controller(FXController controller){
        this.controller = controller;
        return this;
    }

    public ZoeScene build(){
        if( controller == null ) {
            controller = new FXController();
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setController(controller);
        Parent root=null;
        try {
            root = (Parent) loader.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 500, 375);
        ZoeScene zoeScene = new ZoeScene();
        zoeScene.setScene(scene);
        ZoeToolBar toolBar = new ZoeToolBar();
        Pane pane = (Pane) root;
        pane.getChildren().add(toolBar);
        toolBar.setController(controller);
        controller.setScene(scene);
        controller.bindDataContext(context);
        return zoeScene;
    }

}
