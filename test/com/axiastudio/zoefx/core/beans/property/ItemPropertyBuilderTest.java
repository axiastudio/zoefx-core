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
        book.title = "to compile";

        Property titleProperty = ItemPropertyBuilder.create(String.class).bean(book).field("title").build();

        String aString="";
        SimpleStringProperty simpleStringProperty = new SimpleStringProperty(aString);

        titleProperty.bindBidirectional(simpleStringProperty);

        simpleStringProperty.set("Anna Karenina");
        //titleProperty.setValue("Anna Karenina");

        System.out.println(simpleStringProperty.getValue());
        System.out.println(book.title);

        assert simpleStringProperty.get().equals(titleProperty.getValue());

    }

    @Test
    public void testSimpleOnSimple() throws Exception {
        Book book = new Book();
        book.title = null;

        SimpleStringProperty leftProperty = new SimpleStringProperty(book.title);
        SimpleStringProperty rightProperty = new SimpleStringProperty();

        rightProperty.bindBidirectional(leftProperty);

        System.out.println(rightProperty);
        leftProperty.setValue("setValue");
        System.out.println(rightProperty);
        book.title = "book.title";
        System.out.println(rightProperty);

    }
}
