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

import com.axiastudio.zoefx.desktop.controller.Controller;
import com.axiastudio.zoefx.desktop.events.DataSetEvent;
import com.axiastudio.zoefx.desktop.events.DataSetEventListener;
import com.axiastudio.zoefx.desktop.skins.Skins;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:10
 */
public class ZToolBar extends ToolBar implements DataSetEventListener {

    private Controller controller;
    private Map<String, Button> buttons = new HashMap<String, Button>();
    private String[] buttonNames = {"first", "previous", "COUNTER", "next", "last", "add", "delete", "save", "cancel", "print", "refresh", "search", "info"}; //, "console"};
    private Label counterLabel;

    private SimpleBooleanProperty isOnlyOne = new SimpleBooleanProperty(Boolean.FALSE);
    private SimpleBooleanProperty isDirty = new SimpleBooleanProperty(Boolean.FALSE);
    private SimpleBooleanProperty isBOF = new SimpleBooleanProperty(Boolean.FALSE);
    private SimpleBooleanProperty isEOF = new SimpleBooleanProperty(Boolean.FALSE);

    private SimpleBooleanProperty canSelect = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canInsert = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canUpdate = new SimpleBooleanProperty(Boolean.TRUE);
    private SimpleBooleanProperty canDelete = new SimpleBooleanProperty(Boolean.TRUE);

    public ZToolBar(ResourceBundle bundle) {
        this.setId("navigationBar");
        initNavBar(bundle);
    }

    private void initNavBar(ResourceBundle bundle){

        for( String buttonName: buttonNames ){
            if( buttonName.equals("COUNTER") ){
                counterLabel = new Label();
                counterLabel.setMinWidth(80);
                counterLabel.setAlignment(Pos.BASELINE_CENTER);
                counterLabel.setId("counterNavLabel");
                counterLabel.setText("0/0");
                getItems().add(counterLabel);
            } else {
                Button button = new Button();
                button.setId(buttonName + "NavButton");

                if( false ) {
                    button.setMinWidth(70.0);
                    button.setText(bundle.getString("toolbar." + buttonName + "_short"));
                }

                if( Skins.getActiveSkin().noIcons() ) {
                    button.setMinWidth(70.0);
                    button.setText(bundle.getString("toolbar." + buttonName + "_short"));
                }
                String tooltipText = bundle.getString("toolbar." + buttonName);
                button.setTooltip(new Tooltip(tooltipText));
                buttons.put(buttonName, button);
                getItems().add(button);
            }
        }
    }

    public boolean getCanSelectProperty() {
        return canSelect.get();
    }

    public SimpleBooleanProperty canSelectProperty() {
        return canSelect;
    }

    public boolean getCanInsert() {
        return canInsert.get();
    }

    public SimpleBooleanProperty canInsertProperty() {
        return canInsert;
    }

    public boolean getCanUpdate() {
        return canUpdate.get();
    }

    public SimpleBooleanProperty canUpdateProperty() {
        return canUpdate;
    }

    public boolean getCanDelete() {
        return canDelete.get();
    }

    public SimpleBooleanProperty canDeleteProperty() {
        return canDelete;
    }

    public void setController(Controller controller){

        this.controller = controller;

        // handlers
        buttons.get("first").setOnAction(this.controller.handlerGoFirst);
        buttons.get("previous").setOnAction(this.controller.handlerGoPrevious);
        buttons.get("next").setOnAction(this.controller.handlerGoNext);
        buttons.get("last").setOnAction(this.controller.handlerGoLast);

        buttons.get("cancel").setOnAction(this.controller.handlerCancel);
        buttons.get("add").setOnAction(this.controller.handlerAdd);

        buttons.get("print").setOnAction(this.controller.handlerPrint);

        buttons.get("refresh").setOnAction(this.controller.handlerRefresh);
        buttons.get("search").setOnAction(this.controller.handlerSearch);

        buttons.get("info").setOnAction(this.controller.handlerInfo);
        //buttons.get("console").setOnAction(this.controller.handlerConsole);

        // status
        buttons.get("first").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("previous").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("next").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));
        buttons.get("last").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));

        buttons.get("cancel").disableProperty().bind(isDirty.not());
        buttons.get("save").disableProperty().bind(isDirty.not().or(canUpdateProperty().not()));
        buttons.get("search").disableProperty().bind(canSelectProperty().not());

        // Scene mode customs
        if( controller.getMode().equals(ZSceneMode.WINDOW) ){
            buttons.get("add").disableProperty().bind(isDirty.or(canInsertProperty().not()));
            buttons.get("delete").disableProperty().bind(isDirty.or(canDeleteProperty().not()));
            buttons.get("save").setOnAction(this.controller.handlerSave);
            buttons.get("delete").setOnAction(this.controller.handlerDelete);
        }
        else if( controller.getMode().equals(ZSceneMode.DIALOG) ){
            buttons.get("add").setDisable(Boolean.TRUE); //.disableProperty().bind(new SimpleBooleanProperty(Boolean.TRUE));
            buttons.get("delete").setDisable(Boolean.TRUE); //.disableProperty().bind(new SimpleBooleanProperty(Boolean.TRUE));
            buttons.get("save").setId("acceptNavButton");
            buttons.get("save").setOnAction(this.controller.handlerConfirm);
        }

    }

    private void refresh(){

        if( controller == null ){
            return;
        }
        //List<Object> store = this.controller.getStore();

        isOnlyOne.setValue(controller.getDataset().size()==1);
        isDirty.setValue(controller.getDataset().isDirty());
        isBOF.setValue(controller.getDataset().getCurrentIndex() == 0);
        isEOF.setValue(controller.getDataset().getCurrentIndex() == controller.getDataset().size()-1);

        // counter
        String text = (controller.getDataset().getCurrentIndex() + 1) + "/" + controller.getDataset().size();
        counterLabel.setText(text);

    }

    @Override
    public void dataSetEventHandler(DataSetEvent event) {
        Logger.getLogger(this.getClass().getName()).log(Level.FINE, "{0} event handled", event.getEventType().getName());
        if( event.getEventType().equals(DataSetEvent.INDEX_CHANGED) ){
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.GET_DIRTY) ){
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.COMMITED) ){
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.STORE_CHANGED) ){
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.REVERTED) ){
            refresh();
        } else if( event.getEventType().equals(DataSetEvent.DELETED) ){
            refresh();
        }
    }
}