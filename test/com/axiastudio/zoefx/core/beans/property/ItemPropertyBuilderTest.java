package com.axiastudio.zoefx.core.beans.property;

import com.axiastudio.zoefx.demo.Book;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import org.junit.Test;

/**
 * User: tiziano
 * Date: 14/04/14
 * Time: 15:08
 */
public class ItemPropertyBuilderTest {


    /*class Book {
        String title;
        String description;
    }*/

    @Test
    public void test() throws Exception {

        Book book = new Book();
        book.title = "";

        Property titleProperty = ItemPropertyBuilder.create().bean(book).property("title").build();

        String aString="";
        SimpleStringProperty simpleStringProperty = new SimpleStringProperty(aString);

        titleProperty.bindBidirectional(simpleStringProperty);

        titleProperty.setValue("Anna Karenina");

        assert aString.equals(book.title);

    }
}
