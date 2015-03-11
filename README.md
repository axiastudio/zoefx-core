ZoeFX
=====

ZoeFX is a MVC framework based on JavaFX.

Step 1 - define the entities
====================

In ZoeFX an entity can be a simple class with getters and setters or with public fields.

```java
public class Book {
    public String title;
    public String description;
    public Genre genre;
    public Author author;
}
```

The entity can live in a persistence container, but this is not mandatory.

```java
@Entity
public class Book {
    @Id
    public Long id;
    @Column
    public String title;
    @Column
    public String description;
    @Column
    public Genre genre;
    @ManyToOne
    public Author author;
}
```

Step 2 - design the forms
=========================

The forms in ZoeFX are designed with the [Oracle Scene Builder](http://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html). Every component that can hold a value can be linked to a field thru the fx:id property.

![Scene Builder](http://blog.lattisi.com/blog/wp-content/uploads/2014/08/zfx-01.png)

Step 3 - put all together
=========================

The ZSceneBuilder build a scene binding the entities to the UI.

```java
Database database = new NoPersistenceDatabaseImpl();
Utilities.registerUtility(database, Database.class);

primaryStage.setScene(ZSceneBuilder.create()
        .url(QuickStart.class.getResource("books.fxml"))
        .controller(new Controller())
        .manager(database.createManager(Book.class))
        .build());

primaryStage.setTitle("Zoe FX Framework - Quick start Books");
primaryStage.show();
```

![ZoeFX form](http://blog.lattisi.com/blog/wp-content/uploads/2014/08/zfx-02.png)

In this sample the entities are stored in memory, and the application uses only the jre.

Note: the [zoefx-persistence](https://github.com/axiastudio/zoefx-persistence) package is a JPA2 implementation (Hibernate):

```java
Database database = new JPADatabaseImpl();
database.open("persistence-unit");
```

Resources
=========

* [brief introduction](https://github.com/axiastudio/zoefx-core/wiki)
* [slide (italian)](http://www.slideshare.net/lattisi/zoefx-un-framework-mvc-per-javafx)
* [one-file-app sample](https://github.com/axiastudio/zoefx-core/blob/master/src/com/axiastudio/zoefx/demo/QuickStart.java)