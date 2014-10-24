package com.axiastudio.zoefx.core.console;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.controller.Controller;
import com.axiastudio.zoefx.core.script.ScriptEngine;
import com.axiastudio.zoefx.core.script.JSEngineImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 31/03/14
 * Time: 10:38
 */
public class ConsoleController implements Initializable {

    private Controller controller;

    @FXML private ToolBar toolBar;
    @FXML private TextArea source;
    @FXML private TextArea output;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Button button = new Button();
        button.setId("executeConsoleButton");
        toolBar.getItems().add(button);
        button.setOnAction(this.handlerExecute);

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private EventHandler<ActionEvent> handlerExecute = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            // there's a script engine?
            ScriptEngine engine = Utilities.queryUtility(ScriptEngine.class);
            if( engine == null ){
                engine = new JSEngineImpl();
            }

            String sourceText = source.getText();

            Map<String, Object> bindings = new HashMap<>();
            bindings.put("controller", controller);

            Object eval = engine.eval(sourceText, bindings);
            ConsoleController.this.output.appendText(eval + "\n");
        }
    };
}
