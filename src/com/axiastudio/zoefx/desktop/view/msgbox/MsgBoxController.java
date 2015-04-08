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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 29/09/14
 * Time: 12:01
 */
public class MsgBoxController implements Initializable {

    private final int MAX_HEIGHT = 250;
    private final int MIN_HEIGHT = 350;
    @FXML
    private VBox vbox;

    @FXML
    private Label iconLabel;

    @FXML
    private Button cancelMsgBox;

    @FXML
    private Button okMsgBox;

    @FXML
    private Label messageLabel;

    @FXML
    private Label mastheadLabel;

    @FXML
    private TextArea detailsTextArea;

    @FXML
    private TitledPane detailsTitledPane;

    @FXML
    private Accordion detailsAccordion;

    public MsgBoxResponse response = MsgBoxResponse.CANCEL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        detailsAccordion.setVisible(Boolean.FALSE);
        detailsTitledPane.expandedProperty().addListener((observable, oldValue, newValue) -> {
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            if( newValue ) {
                stage.setMinHeight(MIN_HEIGHT);
                stage.setMaxHeight(MIN_HEIGHT);
            } else {
                stage.setMinHeight(MAX_HEIGHT);
                stage.setMaxHeight(MAX_HEIGHT);
            }
        });
    }

    public void config(MsgBoxType type){
        switch(type){
            case CONFIRM:
                break;
            case INFO:
                cancelMsgBox.setVisible(Boolean.FALSE);
                break;
            default:
                break;
        }
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setTitle(String title) {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.setTitle(title);
    }

    public void setMasthead(String masthead) {
        mastheadLabel.setText(masthead);
    }

    public void setDetails(String details) {
        detailsTextArea.setText(details);
        detailsAccordion.setVisible(details.length()>0);
    }

    public MsgBoxResponse getResponse() {
        return response;
    }

    @FXML
    void handlerOk(ActionEvent event) {
        response = MsgBoxResponse.OK;
        close();
    }

    @FXML
    void handlerCancel(ActionEvent event) {
        response = MsgBoxResponse.CANCEL;
        close();
    }

    private void close(){
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}
