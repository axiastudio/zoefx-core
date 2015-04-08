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

package com.axiastudio.zoefx.desktop.console;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.desktop.controller.Controller;
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
            ScriptEngine engine = IOC.queryUtility(ScriptEngine.class);
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
