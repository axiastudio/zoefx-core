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

package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.controller.BaseController;
import com.axiastudio.zoefx.core.controller.Controller;
import com.axiastudio.zoefx.core.db.*;
import com.axiastudio.zoefx.core.skins.Skins;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 22:52
 */
public class ZSceneBuilder<E> {

    private Manager<E> manager=null;
    private EnumSet<Permission> permissions=null;
    private EntityListener<E> listener=null;
    private List<E> store=null;
    private Class entityClass=null;
    private String source=null;
    private URL fxmlUrl;
    private URL propertiesUrl=null;
    private String title;
    private BaseController controller=null;
    private Double width=null;
    private Double height=null;
    private ZSceneMode mode=ZSceneMode.WINDOW;

    public ZSceneBuilder() {
    }

    public static ZSceneBuilder create() {
        return new ZSceneBuilder();
    }

    public static ZSceneBuilder create(Class<?> entityClass) {
        ZSceneBuilder builder = new ZSceneBuilder();
        builder.setEntityClass(entityClass);
        return builder;
    }

    public ZSceneBuilder properties(URL url){
        propertiesUrl = url;
        return this;
    }

    public ZSceneBuilder permission(Permission permission){
        if( permissions==null ){
            permissions = EnumSet.of(permission);
        } else {
            if( !permissions.contains(permission) ) {
                permissions.add(permission);
            }
        }
        return this;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public ZSceneBuilder manager(Manager manager){
        this.manager = manager;
        return this;
    }

    public ZSceneBuilder store(List<E> store) {
        this.store = store;
        return this;
    }

    public ZSceneBuilder title(String title){
        this.title = title;
        return this;
    }

    public String getTitle() {
        if( title == null ) {
            return fxmlUrl.getFile().substring(fxmlUrl.getFile().lastIndexOf("/")+1,
                    fxmlUrl.getFile().lastIndexOf("."));
        }
        return title;
    }

    public ZSceneBuilder url(URL url){
        this.fxmlUrl = url;
        return this;
    }

    public ZSceneBuilder source(String source){
        this.source = source;
        return this;
    }

    public ZSceneBuilder width(Integer width){
        this.width = width.doubleValue();
        return this;
    }

    public ZSceneBuilder width(Double width){
        this.width = width;
        return this;
    }

    public ZSceneBuilder height(Integer height){
        this.height = height.doubleValue();
        return this;
    }

    public ZSceneBuilder height(Double height){
        this.height = height;
        return this;
    }

    public ZSceneBuilder controller(BaseController controller){
        this.controller = controller;
        return this;
    }

    public ZSceneBuilder mode(ZSceneMode mode){
        this.mode = mode;
        return this;
    }

    public ZSceneBuilder<E> listener(EntityListener<E> listener) {
        this.listener = listener;
        return this;
    }

    public Scene build(){
        ResourceBundle bundle = ResourceBundle.getBundle("com.axiastudio.zoefx.core.resources.i18n");
        FXMLLoader loader = new FXMLLoader(fxmlUrl, bundle);
        loader.setResources(bundle);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        if( controller == null ){
            controller = new Controller();
        }
        loader.setController(controller);
        Parent root=null;
        try {
            if( source == null ) {
                loader.setLocation(fxmlUrl);
                root = loader.load();
            } else {
                root = loader.load(new ByteArrayInputStream(source.getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if( root instanceof Region ){
            if( width==null ){
                width = ((Region) root).getPrefWidth();
            }
            if( height==null ){
                height = ((Region) root).getPrefHeight();
            }
        }
        Scene scene = new Scene(root, width, height);
        Skins.getActiveSkin().getStyle().ifPresent(s -> scene.getStylesheets().add(s));
        controller.setScene(scene);
        if( controller instanceof Controller) {
            Controller controller = (Controller) this.controller;
            controller.setMode(mode);
            ZToolBar toolBar = new ZToolBar(bundle);
            if( root instanceof VBox ){
                if( ((VBox) root).getChildren().get(0) instanceof MenuBar) {
                    ((VBox) root).getChildren().add(1, toolBar);
                } else {
                    ((VBox) root).getChildren().add(0, toolBar);
                }
            } else {
                ((Pane) root).getChildren().add(toolBar);
            }

            toolBar.setController(controller);
            if( propertiesUrl != null ){
                try {
                    controller.setBehavior(new Behavior(propertiesUrl.openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            TimeMachine timeMachine = new TimeMachine();
            controller.setTimeMachine(timeMachine);
            if( manager == null ){
                Database database = Utilities.queryUtility(Database.class);
                manager = database.createManager(entityClass);
            }
            if( store == null ) {
                store = manager.getAll();
            }
            DataSet<E> dataset =  DataSetBuilder.create(entityClass).store(store).manager(manager).build();
            dataset.addDataSetEventListener(toolBar);
            dataset.addDataSetEventListener(controller);
            if( listener!= null ){
                dataset.setListener(listener);
            }
            controller.bindDataSet(dataset);

            if( permissions!=null ){
                dataset.setCanSelect(permissions.contains(Permission.SELECT));
                dataset.setCanInsert(permissions.contains(Permission.INSERT));
                dataset.setCanUpdate(permissions.contains(Permission.UPDATE));
                dataset.setCanDelete(permissions.contains(Permission.DELETE));
            }
            toolBar.canSelectProperty().bind(dataset.canSelectProperty());
            toolBar.canInsertProperty().bind(dataset.canInsertProperty());
            toolBar.canUpdateProperty().bind(dataset.canUpdateProperty());
            toolBar.canDeleteProperty().bind(dataset.canDeleteProperty());
        }
        return scene;
    }

}
