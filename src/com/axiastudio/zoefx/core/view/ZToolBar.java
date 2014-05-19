package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.FXController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:10
 */
public class ZToolBar extends ToolBar {

    private FXController controller;
    private Map<String, Button> buttons = new HashMap<String, Button>();
    private String[] buttonNames = {"first", "previous", "COUNTER", "next", "last", "save", "cancel", "add", "delete", "console"};
    private Label counterLabel;

    private SimpleBooleanProperty isOnlyOne = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isDirty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isBOF = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isEOF = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isDialog = new SimpleBooleanProperty(false);

    public ZToolBar() {
        this.setId("navigationBar");
        initNavBar();
    }

    private void initNavBar(){

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
                button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/" + buttonName + ".png"))));
                buttons.put(buttonName, button);
                getItems().add(button);
            }
        }
    }

    public void setController(FXController controller){

        this.controller = controller;

        // handlers
        buttons.get("first").setOnAction(this.controller.handlerGoFirst);
        buttons.get("previous").setOnAction(this.controller.handlerGoPrevious);
        buttons.get("next").setOnAction(this.controller.handlerGoNext);
        buttons.get("last").setOnAction(this.controller.handlerGoLast);

        buttons.get("cancel").setOnAction(this.controller.handlerCancel);
        buttons.get("save").setOnAction(this.controller.handlerSave);
        buttons.get("add").setOnAction(this.controller.handlerAdd);

        buttons.get("console").setOnAction(this.controller.handlerConsole);

        // status
        buttons.get("first").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("previous").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("next").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));
        buttons.get("last").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));

        buttons.get("cancel").disableProperty().bind(isDirty.not());
        buttons.get("save").disableProperty().bind(isDirty.not());

        buttons.get("add").disableProperty().bind(isDirty.or(isDialog));
        buttons.get("delete").disableProperty().bind(isDirty.or(isDialog));

        // dialog icons
        if( controller.getMode().equals(ZSceneMode.DIALOG) ){
            buttons.get("save").setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/checkmark.png"))));
        }

    }

    public void refresh(){

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

        // ZScene mode
        isDialog.setValue(controller.getMode().equals(ZSceneMode.DIALOG));


    }
}