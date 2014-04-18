package com.axiastudio.zoefx.demo;

import com.axiastudio.zoefx.core.ZoeApplication;
import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.core.validators.ValidatorBuilder;
import com.axiastudio.zoefx.core.validators.Validators;
import com.axiastudio.zoefx.core.view.DataContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tiziano
 * Date: 18/04/14
 * Time: 00:36
 */
public class StartDemoAppl extends ZoeApplication {

    public static void main(String args[]){

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
        DataContext<Person> pDC = new DataContext<Person>(persons); // TODO: builder?
        URL pFU = StartDemoAppl.class.getResource("/com/axiastudio/zoefx/demo/persons.fxml");
        ZoeApplication.setPrimaryScene(pDC, pFU);

        List<Book> books = new ArrayList<Book>();
        books.add(karenina);
        books.add(wnp);
        books.add(yos);
        DataContext<Book> bDC = new DataContext<Book>(books);
        URL bFU = StartDemoAppl.class.getResource("/com/axiastudio/zoefx/demo/books.fxml");

        ZoeApplication.launch(StartDemoAppl.class, args);

    }
}
