package com.axiastudio.zoefx.core;

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

    @Override
    public void start(Stage stage) throws Exception {
        ZoeScene scene = ZoeSceneBuilder.build(url, dataContext);
        stage.setScene(scene);
        stage.show();
    }

    public static void setPrimaryScene(DataContext personDataContext, URL personsFxmlUrl) {
        dataContext = personDataContext;
        url = personsFxmlUrl;
    }


}
