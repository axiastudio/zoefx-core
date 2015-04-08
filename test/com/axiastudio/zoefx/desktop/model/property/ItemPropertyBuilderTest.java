/*
 * Copyright (c) 2014, AXIA Studio (Tiziano Lattisi) - http://www.axiastudio.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the AXIA Studio nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY AXIA STUDIO ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL AXIA STUDIO BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.axiastudio.zoefx.desktop.model.property;

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
