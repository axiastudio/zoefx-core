package com.axiastudio.zoefx.core.controller;


import com.axiastudio.zoefx.core.view.ZSceneBuilder;

import java.util.HashMap;

/**
 * User: tiziano
 * Date: 07/05/14
 * Time: 08:37
 */
public class Controllers {

    private static HashMap<ZSceneBuilder, FXController> controllers = new HashMap<>();

    /**
     * Registers the controller for the given scene builder.
     *
     * @param sceneBuilder The class of the entity
     * @param controller The scene controller
     *
     */
    public static void registerController(ZSceneBuilder sceneBuilder, FXController controller){
        Controllers.controllers.put(sceneBuilder, controller);
    }

    /**
     * Query the controller for the given scene builder.
     *
     * @param sceneBuilder The scene builder
     * @return  The controller
     *
     */
    public static FXController queryController(ZSceneBuilder sceneBuilder){
        FXController controller = Controllers.controllers.get(sceneBuilder);
        if( controller == null ){
            controller = new FXController();
        }
        return controller;
    }
}
