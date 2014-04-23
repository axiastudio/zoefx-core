package com.axiastudio.zoefx.core;

import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.view.DataContext;
import com.axiastudio.zoefx.core.view.ZoeScene;
import com.axiastudio.zoefx.core.view.ZoeSceneBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.URL;

/**
 * User: tiziano
 * Date: 18/04/14
 * Time: 00:28
 */
public class ZoeApplication extends Application {

    private static DataContext dataContext = null;
    private static URL url = null;
    private static FXController controller = null;

    @Override
    public void start(Stage stage) throws Exception {
        ZoeScene zoeScene = ZoeSceneBuilder.create().datacontext(dataContext).url(url).controller(controller).build();
        stage.setScene(zoeScene.getScene());
        stage.show();
    }

    public static void setPrimaryScene(DataContext dataContext) {
        setPrimaryScene(dataContext, null, null);
    }

    public static void setPrimaryScene(DataContext dataContext, URL url) {
        setPrimaryScene(dataContext, url, null);
    }

    public static void setPrimaryScene(DataContext dateContext, URL url, FXController controller) {
        ZoeApplication.dataContext = dateContext;
        ZoeApplication.url = url;
        ZoeApplication.controller = controller;
    }


}
