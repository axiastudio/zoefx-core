package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.controller.Controller;
import com.axiastudio.zoefx.core.db.*;
import com.axiastudio.zoefx.core.model.beans.EntityBuilder;
import com.axiastudio.zoefx.core.view.ZSceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class QuickStart extends Application {

    public static class Book {
        public String title;
        public String description;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        NoPersistenceDatabaseImpl database = new NoPersistenceDatabaseImpl();
        Utilities.registerUtility(database, Database.class);
        Manager<Book> manager = database.createManager(Book.class);

        manager.commit(EntityBuilder.create(Book.class).set("title", "Anna Karenina")
                .set("description", "A very long book...").build());

        manager.commit(EntityBuilder.create(Book.class).set("title", "War and peace")
                .set("description", "Another long book...").build());

        String fxml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<?import javafx.scene.control.*?>\n" +
                "<?import java.lang.*?>\n" +
                "<?import javafx.scene.layout.*?>\n" +
                "<AnchorPane id=\"AnchorPane\" maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"400.0\" \n" +
                "    minWidth=\"400.0\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
                "  <children>\n" +
                "    <TextField fx:id=\"title\" layoutX=\"14.0\" layoutY=\"69.0\" prefWidth=\"200.0\" />\n" +
                "    <TextArea fx:id=\"description\" layoutX=\"14.0\" layoutY=\"104.0\" prefWidth=\"200.0\" wrapText=\"true\" />\n" +
                "  </children>\n" +
                "</AnchorPane>";

        Scene scene = ZSceneBuilder.create()
                .source(fxml)
                .controller(new Controller())
                .manager(database.createManager(Book.class))
                .build()
                .getScene();

        primaryStage.setTitle("Zoe FX Framework - Quick start Books");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        Application.launch(QuickStart.class, args);
    }
}
