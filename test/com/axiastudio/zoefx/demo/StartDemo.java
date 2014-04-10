package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.view.DataContext;
import com.axiastudio.zoefx.view.ZoeSceneBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:38
 */
public class StartDemo extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{

        Author lev = new Author();
        lev.name = "Lev";
        lev.surname = "Tolstoj";

        Author marquez = new Author();
        marquez.name = "Gabriel García";
        marquez.surname = "Márquez";

        Book karenina = new Book();
        karenina.title = "Anna Karenina";
        karenina.description = "A very long book...";
        karenina.finished = true;
        karenina.genre = Genre.ROMANCE;
        karenina.author = lev;

        Book wnp = new Book();
        wnp.title = "War and peace";
        wnp.description = "Another long book...";
        wnp.finished = false;
        wnp.genre = Genre.HISTORIC;
        wnp.author = lev;

        Book yearsofsolitude = new Book();
        yearsofsolitude.title = "100 years of solitude";
        yearsofsolitude.description = "A beautiful book.";
        yearsofsolitude.finished = false;
        yearsofsolitude.genre = Genre.ROMANCE;
        yearsofsolitude.author = marquez;

        lev.books.add(karenina);
        lev.books.add(wnp);
        marquez.books.add(yearsofsolitude);

        List<Book> books = new ArrayList<Book>();
        books.add(karenina);
        books.add(wnp);
        books.add(yearsofsolitude);
        DataContext<Book> bookDataContext = new DataContext<Book>(books);
        URL booksFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/books.fxml");
        Scene booksScene = ZoeSceneBuilder.build(booksFxmlUrl, bookDataContext);
        primaryStage.setTitle("Zoe FX Framework - Books");
        primaryStage.setScene(booksScene);
        primaryStage.show();

        List<Author> authors = new ArrayList<Author>();
        authors.add(lev);
        authors.add(marquez);
        DataContext<Author> authorDataContext = new DataContext<>(authors);
        URL authorsFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/authors.fxml");
        Scene authorsScene = ZoeSceneBuilder.build(authorsFxmlUrl, authorDataContext);
        Stage stage = new Stage();
        stage.setTitle("Zoe FX Framework - Authors");
        stage.setScene(authorsScene);
        stage.show();


    }

    public static void main(String[] args){
        Application.launch(StartDemo.class, args);
    }

}
