package com.axiastudio.zoefx.scriptengine.console;

import com.axiastudio.zoefx.controller.FXController;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 31/03/14
 * Time: 10:38
 */
public class ConsoleController implements Initializable {

    private FXController controller;

    @FXML private ToolBar toolBar;
    @FXML private TextArea source;
    @FXML private TextArea output;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Button button = new Button();
        button.setId("executeConsoleButton");
        button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/com/axiastudio/zoefx/resources/cogs.png"))));
        toolBar.getItems().add(button);
        button.setOnAction(this.handlerExecute);

    }

    public void setController(FXController controller) {
        this.controller = controller;
    }

    private EventHandler<ActionEvent> handlerExecute = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {

            // redirect System.out
            PrintStream originalOut = System.out;
            ByteArrayOutputStream newOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(newOut));

            // binding locals and evaluate script
            Binding binding = new Binding();
            binding.setVariable("controller", controller);
            GroovyShell shell = new GroovyShell(binding);
            String groovySource = source.getText();
            shell.evaluate(groovySource);
            try {
                newOut.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String groovyOutput = newOut.toString();
            output.appendText(groovyOutput);

            // restore original System.out
            System.setOut(originalOut);
        }
    };
}
