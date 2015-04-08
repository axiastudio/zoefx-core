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

package com.axiastudio.zoefx.desktop.view.msgbox;

import com.axiastudio.zoefx.desktop.skins.Skins;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 29/09/14
 * Time: 12:04
 */
public class MsgBoxBuilder {

    private String title="";
    private String masthead="";
    private String message="";
    private String details="";


    public static MsgBoxBuilder create(){
        return new MsgBoxBuilder();
    }

    public MsgBoxBuilder title(String title){
        this.title = title;
        return this;
    }

    public MsgBoxBuilder masthead(String masthead){
        this.masthead = masthead;
        return this;
    }

    public MsgBoxBuilder message(String message){
        this.message = message;
        return this;
    }

    public MsgBoxBuilder details(String details){
        this.details = details;
        return this;
    }

    public MsgBoxResponse showConfirm(){
        return showDialog(MsgBoxType.CONFIRM);
    }

    public MsgBoxResponse showInfo(){
        return showDialog(MsgBoxType.INFO);
    }

    private MsgBoxResponse showDialog(MsgBoxType type) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.axiastudio.zoefx.desktop.resources.i18n");
        URL url = getClass().getResource("/com/axiastudio/zoefx/desktop/view/msgbox/msgbox.fxml");
        FXMLLoader loader = new FXMLLoader(url, bundle);
        loader.setResources(bundle);
        loader.setLocation(url);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        MsgBoxController controller = new MsgBoxController();
        loader.setController(controller);
        Parent root=null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller.setMessage(message);
        controller.setMasthead(masthead);
        controller.setDetails(details);
        controller.config(type);
        Scene scene = new Scene(root);
        Skins.getActiveSkin().getStyle().ifPresent(s -> scene.getStylesheets().add(s));
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.showAndWait();
        return controller.getResponse();
    }

}
