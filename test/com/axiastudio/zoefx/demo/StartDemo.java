package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.ZoeApplication;
import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.core.validators.ValidatorBuilder;
import com.axiastudio.zoefx.core.validators.Validators;
import com.axiastudio.zoefx.core.view.DataContext;
import com.axiastudio.zoefx.core.view.ZoeScene;
import com.axiastudio.zoefx.core.view.ZoeSceneBuilder;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:38
 */
public class StartDemo extends ZoeApplication {

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
        Loan loan = EntityBuilder.create(Loan.class).set("book", karenina).set("person", tiziano)
                .set("note", "To return- ;-)").build();
        tiziano.loans.add(loan);

        Validators.bindValidator(Book.class, "title", ValidatorBuilder.create().minLength(2).maxLength(5).build());

        List<Person> persons = new ArrayList<Person>();
        persons.add(tiziano);
        DataContext<Person> personDataContext = new DataContext<Person>(persons);
        URL personsFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/persons.fxml");
        ZoeScene personsScene = ZoeSceneBuilder.create().url(personsFxmlUrl).datacontext(personDataContext).build();
        Stage personsStage = new Stage();
        personsStage.setTitle("Zoe FX Framework - Persons");
        personsStage.setScene(personsScene.getScene());
        personsStage.show();

        List<Book> books = new ArrayList<Book>();
        books.add(karenina);
        books.add(wnp);
        books.add(yos);
        DataContext<Book> bookDataContext = new DataContext<Book>(books);
        URL booksFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/books.fxml");
        ZoeScene booksScene = ZoeSceneBuilder.create().url(booksFxmlUrl).datacontext(bookDataContext).build();
        primaryStage.setTitle("Zoe FX Framework - Books");
        primaryStage.setScene(booksScene.getScene());
        primaryStage.show();

        List<Author> authors = new ArrayList<Author>();
        authors.add(lev);
        authors.add(marquez);
        DataContext<Author> authorDataContext = new DataContext<>(authors);
        URL authorsFxmlUrl = this.getClass().getResource("/com/axiastudio/zoefx/demo/authors.fxml");
        ZoeScene authorsScene = ZoeSceneBuilder.create().url(authorsFxmlUrl).datacontext(authorDataContext).build();
        Stage authorsStage = new Stage();
        authorsStage.setTitle("Zoe FX Framework - Authors");
        authorsStage.setScene(authorsScene.getScene());
        authorsStage.show();


    }

    public static void main(String[] args){
        ZoeApplication.launch(StartDemo.class, args);
    }

}
