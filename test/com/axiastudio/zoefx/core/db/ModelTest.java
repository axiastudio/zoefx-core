package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.view.Model;
import org.junit.Test;

/**
 * User: tiziano
 * Date: 21/03/14
 * Time: 14:32
 */
public class ModelTest {

    class Book{
        private String title;
        private String description;
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    @Test
    public void testFXModel() throws Exception {
        Book book = new Book();
        book.setTitle("Anna Karenina");
        book.setDescription("A very long book...");

        Model<Book> model = new Model<Book>(book);
    }
}
