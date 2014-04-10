package com.axiastudio.zoefx.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: tiziano
 * Date: 04/04/14
 * Time: 09:23
 */
public class Author {
    public Long id;
    public String name;
    public String surname;
    public Collection<Book> books = new ArrayList<Book>();

}
