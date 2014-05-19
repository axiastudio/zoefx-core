package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.BaseController;
import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.db.DataSet;
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
public class ZSceneBuilder {

    private DataSet dataset;
    private URL url;
    private BaseController controller=null;
    private Integer width=500;
    private Integer height=375;

    public ZSceneBuilder() {
    }

    public static ZSceneBuilder create() {
        return new ZSceneBuilder();
    }

    public ZSceneBuilder dataset(DataSet dataset){
        this.dataset = dataset;
        return this;
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        if( controller != null ) {
            loader.setController(controller);
        }
        Parent root=null;
        try {
            root = (Parent) loader.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, width, height);
        ZScene zScene = new ZScene();
        zScene.setScene(scene);
        if( controller instanceof FXController ) {
            FXController fxController = (FXController) controller;
            ZToolBar toolBar = new ZToolBar();
            Pane pane = (Pane) root;
            pane.getChildren().add(toolBar);
            toolBar.setController(fxController);
            fxController.setScene(scene);
            fxController.bindDataSet(dataset);
        }
        return zScene;
    }

}
