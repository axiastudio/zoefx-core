package com.axiastudio.zoefx.core.view;

import java.util.HashMap;

/**
 * User: tiziano
 * Date: 07/05/14
 * Time: 08:37
 */
public class SceneBuilders {

    private static HashMap<Class, ZSceneBuilder> scenebuilders = new HashMap<>();

    /**
     * Registers the scene builder for the given entity class.
     *
     * @param entityClass The class of the entity
     * @param builder The scene builder
     *
     */
    public static void registerForm(Class entityClass, ZSceneBuilder builder){
        SceneBuilders.scenebuilders.put(entityClass, builder);
    }

    /**
     * Query the scene builder for the given entity.
     *
     * @param entityClass The class of the entity
     * @return  The utility
     *
     */
    public static ZSceneBuilder queryForm(Class entityClass){
        return SceneBuilders.scenebuilders.get(entityClass);
    }
}
