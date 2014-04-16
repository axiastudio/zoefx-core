package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.core.validators.ValidatorBuilder;
import com.axiastudio.zoefx.core.validators.Validators;
import com.axiastudio.zoefx.core.view.DataContext;
import com.axiastudio.zoefx.core.view.ZoeSceneBuilder;
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

        Author lev = EntityBuilder.create(Author.class).set("name", "Lev").set("surname", "Tolstoj").build();
        Author marquez = EntityBuilder.create(Author.class).set("name", "Gabriel García").set("surname", "Márquez").build();

        Book karenina = EntityBuilder.create(Book.class).set("title", "Anna Karenina").set("finished", true)
                .set("description", "A very long book...").set("genre", Genre.ROMANCE).set("author", lev).build();

        Book wnp = EntityBuilder.create(Book.class).set("title", "War and peace").set("finished", false)
                .set("description", "Another long book...").set("genre", Genre.HISTORIC).set("author", lev).build();

        Book yos = EntityBuilder.create(Book.class).set("title", "100 years of solitude").set("finished", false)
                .set("description", "A beautiful book.").set("genre", Genre.ROMANCE).set("author", marquez).build();

        // beacause we don't have a db...
        lev.books.add(karenina);
        lev.books.add(wnp);
        marquez.books.add(yos);

        Person tiziano = EntityBuilder.create(Person.class).set("name", "Tiziano").set("surname", "Lattisi").build();
        Loan loanKarenina = new Loan();
        loanKarenina.book = karenina;
        loanKarenina.person = tiziano;
        loanKarenina.note = "To return. ;-)";
        tiziano.loans.add(loanKarenina);

        Validators.bindValidator(Book.class, "title", ValidatorBuilder.create().minLength(2).maxLength(5).build());

        List<Person> persons = new ArrayList<Person>();
        persons.add(tiziano);
        DataContext<Person> personDataContext = new DataContext<Person>(persons);
        URL personsFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/persons.fxml");
        Scene personsScene = ZoeSceneBuilder.build(personsFxmlUrl, personDataContext);
        Stage personsStage = new Stage();
        personsStage.setTitle("Zoe FX Framework - Persons");
        personsStage.setScene(personsScene);
        personsStage.show();

        List<Book> books = new ArrayList<Book>();
        books.add(karenina);
        books.add(wnp);
        books.add(yos);
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
        Stage authorsStage = new Stage();
        authorsStage.setTitle("Zoe FX Framework - Authors");
        authorsStage.setScene(authorsScene);
        authorsStage.show();


    }

    public static void main(String[] args){
        Application.launch(StartDemo.class, args);
    }

}
