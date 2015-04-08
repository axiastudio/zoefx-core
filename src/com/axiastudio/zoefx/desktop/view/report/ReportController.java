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

package com.axiastudio.zoefx.desktop.view.report;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.desktop.model.beans.LookupStringConverter;
import com.axiastudio.zoefx.desktop.controller.BaseController;
import com.axiastudio.zoefx.core.report.ReportEngine;
import com.axiastudio.zoefx.core.report.ReportTemplate;
import com.axiastudio.zoefx.core.report.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 05/09/14
 * Time: 11:34
 */
public class ReportController<T> extends BaseController {

    @FXML
    private ChoiceBox<ReportTemplate> templates;

    @FXML
    private Button printButton;

    @FXML
    private Button exportButton;

    private Class entityClass;
    private List<T> store;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ReportEngine reportEngine = IOC.queryUtility(ReportEngine.class);
        printButton.setDisable(!reportEngine.canPrint());
        printButton.setText(resources.getString("report.print_button"));
        exportButton.setDisable(!reportEngine.canExportToPdf());
        exportButton.setText(resources.getString("report.export_to_pdf_button"));
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
        // XXX: use ifPresent
        ObservableList<ReportTemplate> choices = FXCollections.observableArrayList(Reports.getTemplates(entityClass).get());
        templates.setConverter(new LookupStringConverter<>("title"));
        templates.setItems(choices);
        templates.setValue(choices.get(0));
    }

    public void setStore(List<T> store){
        this.store = store;
    }

    @FXML
    void exportToPdf(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save to...");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF file", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        ReportTemplate reportTemplate = templates.getValue();
        ReportEngine reportEngine = IOC.queryUtility(ReportEngine.class);
        reportEngine.toPdf(reportTemplate, store, file);
        ((Stage) getScene().getWindow()).close();
    }

    @FXML
    void print(ActionEvent event) {
        ReportTemplate reportTemplate = templates.getValue();
        ReportEngine reportEngine = IOC.queryUtility(ReportEngine.class);
        reportEngine.toPrinter(reportTemplate, store);
        ((Stage) getScene().getWindow()).close();
    }

}
