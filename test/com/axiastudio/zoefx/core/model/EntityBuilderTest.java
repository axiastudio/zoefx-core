package com.axiastudio.zoefx.core.model;

import com.axiastudio.zoefx.core.model.beans.EntityBuilder;
import com.axiastudio.zoefx.demo.Book;
import com.axiastudio.zoefx.demo.Genre;
import org.junit.Test;

/**
 * User: tiziano
 * Date: 16/04/14
 * Time: 11:48
 */
public class EntityBuilderTest {

    @Test
    public void test() throws Exception {

        Book book = EntityBuilder.create(Book.class).set("title", "Anna Karenina")
                .set("description", "A very long book...").set("genre", Genre.ROMANCE).build();

        assert book instanceof Book;
        assert book.title.equals("Anna Karenina");
        assert book.genre.equals(Genre.ROMANCE);

    }

}
