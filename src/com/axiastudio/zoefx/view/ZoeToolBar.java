package com.axiastudio.zoefx.view;

import com.axiastudio.zoefx.controller.FXController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
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
public class ZoeToolBar extends ToolBar {

    private FXController controller;
    private Map<String, Button> buttons = new HashMap();
    private String[] buttonNames = {"first", "previous", "next", "last", "save", "cancel", "add", "delete", "console"};

    private SimpleBooleanProperty isOnlyOne = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isDirty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isBOF = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isEOF = new SimpleBooleanProperty(false);

    public ZoeToolBar() {
        this.setId("navigationBar");
        initNavBar();
    }

    private void initNavBar(){

        for( String buttonName: buttonNames ){
            Button button = new Button();
            button.setId(buttonName+"NavButton");
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/resources/"+buttonName+".png"))));
            this.buttons.put(buttonName, button);
            this.getItems().add(button);
        }
    }

    public void setController(FXController controller){

        this.controller = controller;

        // handlers
        this.buttons.get("first").setOnAction(this.controller.handlerGoFirst);
        this.buttons.get("previous").setOnAction(this.controller.handlerGoPrevious);
        this.buttons.get("next").setOnAction(this.controller.handlerGoNext);
        this.buttons.get("last").setOnAction(this.controller.handlerGoLast);

        //this.buttons.get("cancel").setOnAction(this.controller.handlerCancel);
        //this.buttons.get("save").setOnAction(this.controller.handlerSave);

        //this.buttons.get("console").setOnAction(this.controller.handlerConsole);

        // status
        this.buttons.get("first").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        this.buttons.get("previous").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        this.buttons.get("next").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));
        this.buttons.get("last").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));

        this.buttons.get("cancel").disableProperty().bind(isDirty.not());
        this.buttons.get("save").disableProperty().bind(isDirty.not());

        this.buttons.get("add").disableProperty().bind(isDirty);
        this.buttons.get("delete").disableProperty().bind(isDirty);


    }

    public void refresh(){

        if( this.controller == null ){
            return;
        }
        //List<Object> store = this.controller.getStore();

        isOnlyOne.setValue(controller.getContext().size()==1);
        isDirty.setValue(controller.getContext().isDirty());
        isBOF.setValue(controller.getContext().getCurrentIndex() == 0);
        isEOF.setValue(controller.getContext().getCurrentIndex() == controller.getContext().size()-1);

    }
}