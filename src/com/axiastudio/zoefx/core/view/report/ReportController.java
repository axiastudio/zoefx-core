package com.axiastudio.zoefx.core.view.report;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * User: tiziano
 * Date: 05/09/14
 * Time: 11:34
 */
public class ReportController<T> implements Initializable {
    private Class entityClass;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("init " + entityClass);
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }
}
