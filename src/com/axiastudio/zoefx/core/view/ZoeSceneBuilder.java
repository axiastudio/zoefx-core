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
public class ZoeSceneBuilder {

    private DataSet context;
    private URL url;
    private BaseController controller=null;
    private Integer width=500;
    private Integer height=375;

    public ZoeSceneBuilder() {
    }

    public static ZoeSceneBuilder create() {
        return new ZoeSceneBuilder();
    }

    public ZoeSceneBuilder datacontext(DataSet context){
        this.context = context;
        return this;
    }

    public ZoeSceneBuilder url(URL url){
        this.url = url;
        return this;
    }

    public ZoeSceneBuilder width(Integer width){
        this.width = width;
        return this;
    }

    public ZoeSceneBuilder height(Integer height){
        this.height = height;
        return this;
    }

    public ZoeSceneBuilder controller(BaseController controller){
        this.controller = controller;
        return this;
    }

    public ZoeScene build(){
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
        ZoeScene zoeScene = new ZoeScene();
        zoeScene.setScene(scene);
        if( controller instanceof FXController ) {
            FXController fxController = (FXController) controller;
            ZoeToolBar toolBar = new ZoeToolBar();
            Pane pane = (Pane) root;
            pane.getChildren().add(toolBar);
            toolBar.setController(fxController);
            fxController.setScene(scene);
            fxController.bindDataContext(context);
        }
        return zoeScene;
    }

}
