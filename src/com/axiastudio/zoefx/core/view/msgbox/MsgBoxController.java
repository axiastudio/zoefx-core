package com.axiastudio.zoefx.core.view.msgbox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
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
    private TitledPane detailsTitledPane;

    public MsgBoxResponse response = MsgBoxResponse.CANCEL;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void setMessage(String message) {
        messageLabel.setText(message);
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
