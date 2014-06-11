package com.axiastudio.zoefx.core.view;

import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.events.DataSetEvent;
import com.axiastudio.zoefx.core.events.DataSetEventListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: tiziano
 * Date: 20/03/14
 * Time: 23:10
 */
public class ZToolBar extends ToolBar implements DataSetEventListener {

    private FXController controller;
    private Map<String, Button> buttons = new HashMap<String, Button>();
    private String[] buttonNames = {"first", "previous", "COUNTER", "next", "last", "add", "delete", "save", "cancel", "refresh", "search", "info", "console"};
    private Label counterLabel;

    private SimpleBooleanProperty isOnlyOne = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isDirty = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isBOF = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isEOF = new SimpleBooleanProperty(false);

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
        buttons.get("add").setOnAction(this.controller.handlerAdd);

        buttons.get("refresh").setOnAction(this.controller.handlerRefresh);
        buttons.get("search").setOnAction(this.controller.handlerSearch);

        buttons.get("info").setOnAction(this.controller.handlerInfo);
        buttons.get("console").setOnAction(this.controller.handlerConsole);

        // status
        buttons.get("first").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("previous").disableProperty().bind(isDirty.or(isOnlyOne).or(isBOF));
        buttons.get("next").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));
        buttons.get("last").disableProperty().bind(isDirty.or(isOnlyOne).or(isEOF));

        buttons.get("cancel").disableProperty().bind(isDirty.not());
        buttons.get("save").disableProperty().bind(isDirty.not());


        // ZScene mode customs
        if( controller.getMode().equals(ZSceneMode.WINDOW) ){
            buttons.get("add").disableProperty().bind(isDirty);
            buttons.get("delete").disableProperty().bind(isDirty);
            buttons.get("save").setOnAction(this.controller.handlerSave);
            buttons.get("delete").setOnAction(this.controller.handlerDelete);
        }
        else if( controller.getMode().equals(ZSceneMode.DIALOG) ){
            buttons.get("add").disableProperty().bind(new SimpleBooleanProperty(true));
            buttons.get("delete").setDisable(true); //.disableProperty().bind(new SimpleBooleanProperty(true));
            buttons.get("save").setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/core/resources/checkmark.png"))));
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