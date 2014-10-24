package com.axiastudio.zoefx.core.view.report;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.model.beans.LookupStringConverter;
import com.axiastudio.zoefx.core.controller.BaseController;
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
        ReportEngine reportEngine = Utilities.queryUtility(ReportEngine.class);
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
        ReportEngine reportEngine = Utilities.queryUtility(ReportEngine.class);
        reportEngine.toPdf(reportTemplate, store, file);
        ((Stage) getScene().getWindow()).close();
    }

    @FXML
    void print(ActionEvent event) {
        ReportTemplate reportTemplate = templates.getValue();
        ReportEngine reportEngine = Utilities.queryUtility(ReportEngine.class);
        reportEngine.toPrinter(reportTemplate, store);
        ((Stage) getScene().getWindow()).close();
    }

}
