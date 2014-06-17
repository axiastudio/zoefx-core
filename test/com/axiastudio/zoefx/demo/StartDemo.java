package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.core.controller.FXController;
import com.axiastudio.zoefx.core.validators.ValidatorBuilder;
import com.axiastudio.zoefx.core.validators.Validators;
import com.axiastudio.zoefx.core.db.DataSet;
import com.axiastudio.zoefx.core.view.SceneBuilders;
import com.axiastudio.zoefx.core.view.ZScene;
import com.axiastudio.zoefx.core.view.ZSceneBuilder;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:38
 */
public class StartDemo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Author lev = EntityBuilder.create(Author.class).set("name", "Lev").set("surname", "Tolstoj").build();
        Author marquez = EntityBuilder.create(Author.class).set("name", "Gabriel García").set("surname", "Márquez").build();

        Book karenina = EntityBuilder.create(Book.class).set("title", "Anna Karenina").set("year", 2000).set("finished", true)
                .set("description", "A very long book...").set("genre", Genre.ROMANCE).set("author", lev).build();

        Book wnp = EntityBuilder.create(Book.class).set("title", "War and peace").set("year", 2000).set("finished", false)
                .set("description", "Another long book...").set("genre", Genre.HISTORIC).set("author", lev).build();

        Book yos = EntityBuilder.create(Book.class).set("title", "100 years of solitude").set("year", 2000).set("finished", false)
                .set("description", "A beautiful book.").set("genre", Genre.ROMANCE).set("author", marquez).build();

        // beacause we don't have a db...
        lev.books.add(karenina);
        lev.books.add(wnp);
        marquez.books.add(yos);

        Person tiziano = EntityBuilder.create(Person.class).set("name", "Tiziano").set("surname", "Lattisi").build();
        Loan loan = EntityBuilder.create(Loan.class).set("book", karenina).set("person", tiziano)
                .set("note", "To return- ;-)").build();
        tiziano.loans.add(loan);

        Validators.bindValidator(Book.class, "title", ValidatorBuilder.create().minLength(2).maxLength(5).build());

        // Primary stage
        List<Book> books = new ArrayList<Book>();
        books.add(karenina);
        books.add(wnp);
        books.add(yos);
        ZSceneBuilder zsbBook = ZSceneBuilder.create().url(StartDemo.class.getResource("/com/axiastudio/zoefx/demo/books.fxml"))
                .controller(new FXController()).dataset(new DataSet<>(books));
        zsbBook = zsbBook.properties(StartDemo.class.getResourceAsStream("book.properties"));
        SceneBuilders.registerSceneBuilder(Book.class, zsbBook);

        // Secondary stage
        List<Person> persons = new ArrayList<>();
        persons.add(tiziano);
        ZSceneBuilder zsbPerson = ZSceneBuilder.create().url(StartDemo.class.getResource("/com/axiastudio/zoefx/demo/persons.fxml"))
                .controller(new FXController()).dataset(new DataSet<>(persons));
        SceneBuilders.registerSceneBuilder(Person.class, zsbPerson);
        zsbPerson = zsbPerson.properties(StartDemo.class.getResourceAsStream("person.properties"));
        ZScene personsScene = zsbPerson.build();
        Stage personsStage = new Stage();
        personsStage.setTitle("Zoe FX Framework - Persons");
        personsStage.setScene(personsScene.getScene());
        personsStage.show();

        // Secondary stage
        List<Author> authors = new ArrayList<>();
        authors.add(lev);
        authors.add(marquez);
        ZSceneBuilder zsbAuthor = ZSceneBuilder.create().url(StartDemo.class.getResource("/com/axiastudio/zoefx/demo/authors.fxml"))
                .controller(new FXController()).dataset(new DataSet<>(authors));
        SceneBuilders.registerSceneBuilder(Author.class, zsbAuthor);
        ZScene authorsScene = zsbAuthor.build();
        /*
        Stage authorsStage = new Stage();
        authorsStage.setTitle("Zoe FX Framework - Authors");
        authorsStage.setScene(authorsScene.getScene());
        authorsStage.show();
        */

    }

    public static void main(String[] args){
        Application.launch(StartDemo.class, args);
    }

}
