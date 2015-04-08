/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.axiastudio.zoefx.desktop.view;

import com.axiastudio.zoefx.desktop.db.DataSet;
import javafx.scene.Scene;

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
     * Query the Scene for the given dataSet.
     *
     * @param dataSet The DataSet
     * @param mode is the Scene mode (Window or dialog)
     * @return  The Scene
     *
     */
    public static Scene queryZScene(DataSet<Object> dataSet, ZSceneMode mode) {
        ZSceneBuilder zsb = SceneBuilders.querySceneBuilder(dataSet);
        if( zsb != null ){
            zsb.mode(mode);
            return zsb.build();
        }
        return null;
    }
    public static Scene queryZScene(DataSet<Object> dataSet) {
        return queryZScene(dataSet, ZSceneMode.WINDOW);
    }

}
