package com.axiastudio.zoefx.core.controller;

import javafx.fxml.Initializable;
import javafx.scene.Scene;

/**
 * User: tiziano
 * Date: 23/04/14
 * Time: 11:56
 */
public abstract class BaseController implements Initializable {

    private Scene scene;

    public void setScene(Scene scene){
        this.scene = scene;
    }

    protected Scene getScene() {
        return scene;
    }

}
