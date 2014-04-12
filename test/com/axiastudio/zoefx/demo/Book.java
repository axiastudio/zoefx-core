package com.axiastudio.zoefx.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 20:55
 */
public class Book {
    public String title;
    public String description;
    public Boolean finished;
    public Genre genre;
    public Author author;
}
