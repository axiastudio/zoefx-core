package com.axiastudio.zoefx.view;

import com.axiastudio.zoefx.controller.FXController;
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

    public static Scene build(URL url, DataContext context){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root=null;
        try {
            root = (Parent) loader.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 500, 375);
        ZoeToolBar toolBar = new ZoeToolBar();
        Pane pane = (Pane) root;
        pane.getChildren().add(toolBar);
        FXController controller = loader.getController();
        toolBar.setController(controller);
        controller.setScene(scene);
        controller.bindDataContext(context);
        return scene;
    }

}
