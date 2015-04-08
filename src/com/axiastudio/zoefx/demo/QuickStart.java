package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.desktop.controller.Controller;
import com.axiastudio.zoefx.core.db.*;
import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.desktop.view.ZSceneBuilder;
import com.axiastudio.zoefx.desktop.db.NoPersistenceDatabaseImpl;
import javafx.application.Application;
import javafx.stage.Stage;

public class QuickStart extends Application {

    public static class Book { public String title; public String description; }

    @Override
    public void start(Stage primaryStage) throws Exception {

        NoPersistenceDatabaseImpl database = new NoPersistenceDatabaseImpl();
        IOC.registerUtility(database, Database.class);
        Manager<Book> manager = database.createManager(Book.class);

        manager.save(EntityBuilder.create(Book.class).set("title", "Anna Karenina")
                .set("description", "A very long book...").build());
        manager.save(EntityBuilder.create(Book.class).set("title", "War and peace")
                .set("description", "Another long book...").build());

        String fxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<?import javafx.scene.control.*?>\n" +
                "<?import java.lang.*?>\n" +
                "<?import javafx.scene.layout.*?>\n" +
                "<AnchorPane xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
                "  <children>" +
                "    <TextField fx:id=\"title\" layoutX=\"14.0\" layoutY=\"69.0\" prefWidth=\"200.0\" />\n" +
                "    <TextArea fx:id=\"description\" layoutX=\"14.0\" layoutY=\"104.0\" prefWidth=\"200.0\" />\n" +
                "  </children>" +
                "</AnchorPane>";

        primaryStage.setScene(ZSceneBuilder.create()
                .source(fxml)
                .controller(new Controller())
                .manager(database.createManager(Book.class))
                .build());
        primaryStage.setTitle("Zoe FX Framework - Quick start Books");
        primaryStage.show();
    }

    public static void main(String[] args){
        Application.launch(QuickStart.class, args);
    }
}
