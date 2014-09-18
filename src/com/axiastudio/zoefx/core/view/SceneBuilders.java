package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.db.DataSet;

import java.util.HashMap;

/**
 * User: tiziano
 * Date: 07/05/14
 * Time: 08:37
 */
public class SceneBuilders {

    private static HashMap<String, ZSceneBuilder> scenebuilders = new HashMap<>();

    /**
     * Registers the scene builder for the given entity class.
     *
     * @param entityClass The class of the entity
     * @param builder The scene builder
     *
     */
    public static void registerSceneBuilder(Class entityClass, ZSceneBuilder builder){
        SceneBuilders.scenebuilders.put(entityClass.getSimpleName(), builder);
    }

    /**
     * Query the scene builder for the given entity.
     *
     * @param entityClass The class of the entity
     * @return  The ZSceneBuilder
     *
     */
    public static ZSceneBuilder querySceneBuilder(Class entityClass){
        return SceneBuilders.scenebuilders.get(entityClass.getSimpleName());
    }

    /**
     * Query the scene builder for the given dataSet.
     *
     * @param dataSet The DataSet
     * @return  The ZSceneBuilder
     *
     */
    public static ZSceneBuilder querySceneBuilder(DataSet<Object> dataSet){
        if( dataSet != null && dataSet.size()>0 ) {
            return querySceneBuilder(dataSet.getEntityClass());
        }
        return null;
    }


    /**
     * Query the ZScene for the given dataSet.
     *
     * @param dataSet The DataSet
     * @param mode is the ZScene mode (Window or dialog)
     * @return  The ZScene
     *
     */
    public static ZScene queryZScene(DataSet<Object> dataSet, ZSceneMode mode) {
        ZSceneBuilder zsb = SceneBuilders.querySceneBuilder(dataSet);
        if( zsb != null ){
            zsb.mode(mode);
            ZScene zScene = zsb.build();
            return zScene;
        }
        return null;
    }
    public static ZScene queryZScene(DataSet<Object> dataSet) {
        return queryZScene(dataSet, ZSceneMode.WINDOW);
    }

}
