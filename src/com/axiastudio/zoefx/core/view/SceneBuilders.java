package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.Controllers;
import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.db.DataSet;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static void registerSceneBuilder(Class entityClass, ZSceneBuilder builder){
        SceneBuilders.scenebuilders.put(entityClass, builder);
    }

    /**
     * Query the scene builder for the given entity.
     *
     * @param entityClass The class of the entity
     * @return  The ZSceneBuilder
     *
     */
    public static ZSceneBuilder querySceneBuilder(Class entityClass){
        return SceneBuilders.scenebuilders.get(entityClass);
    }

    /**
     * Query the scene builder for the given store.
     *
     * @param store The store
     * @return  The ZSceneBuilder
     *
     */
    public static ZSceneBuilder querySceneBuilder(List<Object> store){
        if( store != null && store.size()>0 ) {
            return querySceneBuilder(store.get(0).getClass());
        }
        return null;
    }

    /**
     * Query the ZScene for the given entity.
     *
     * @param entity The entity
     * @return  The ZScene
     *
     */
    public static ZScene queryZScene(Object entity) {
        ZSceneBuilder sceneBuilder = SceneBuilders.querySceneBuilder(entity.getClass());
        FXController controller = Controllers.queryController(sceneBuilder);
        List<Object> store = new ArrayList<>();
        store.add(entity);
        DataSet<Object> dataSet = new DataSet<>(store);
        ZScene zScene = sceneBuilder.dataset(dataSet).controller(controller).build();
        return zScene;
    }

    /**
     * Query the ZScene for the given store.
     *
     * @param store The store
     * @param mode is the ZScene mode (Window or dialog)
     * @return  The ZScene
     *
     */
    public static ZScene queryZScene(List<Object> store, ZSceneMode mode) {
        ZSceneBuilder zsb = SceneBuilders.querySceneBuilder(store);
        FXController controller = Controllers.queryController(zsb);
        DataSet<Object> dataSet = new DataSet<>(store);
        zsb = zsb.dataset(dataSet);
        zsb = zsb.controller(controller);
        zsb.mode(ZSceneMode.DIALOG);
        ZScene zScene = zsb.build();
        return zScene;
    }
    public static ZScene queryZScene(List<Object> store) {
        return queryZScene(store, ZSceneMode.WINDOW);
    }

}
