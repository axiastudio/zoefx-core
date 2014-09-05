package com.axiastudio.zoefx.core.view.report;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.beans.LookupStringConverter;
import com.axiastudio.zoefx.core.report.ReportEngine;
import com.axiastudio.zoefx.core.report.ReportTemplate;
import com.axiastudio.zoefx.core.report.Reports;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 05/09/14
 * Time: 11:34
 */
public class ReportController<T> implements Initializable {

    @FXML
    private ChoiceBox<ReportTemplate> templates;

    private Class entityClass;
    private List<T> store;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init " + entityClass);
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
        ObservableList choices = FXCollections.observableArrayList(Reports.getTemplates(entityClass));
        templates.setConverter(new LookupStringConverter<>("title"));
        templates.setItems(choices);
    }

    public void setStore(List<T> store){
        this.store = store;
    }

    @FXML
    void print(ActionEvent event) {
        ReportTemplate reportTemplate = templates.getValue();
        ReportEngine reportEngine = Utilities.queryUtility(ReportEngine.class);
        reportEngine.printTemplate(reportTemplate, store);
    }
}
