package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.demo.Author;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WithoutDatabaseImplTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Author lev = EntityBuilder.create(Author.class).set("name", "Lev").set("surname", "Tolstoj").build();
        Author marquez = EntityBuilder.create(Author.class).set("name", "Gabriel García").set("surname", "Márquez").build();

        List<Author> authors = new ArrayList<>();
        authors.add(lev);
        authors.add(marquez);

        WithoutDatabaseImpl database = new WithoutDatabaseImpl();
        database.putStore(authors, Author.class);

        Utilities.registerUtility(database, Database.class);

    }

    @Test
    public void testCreateManager() throws Exception {

        Database db = Utilities.queryUtility(Database.class);
        Manager<Author> manager = db.createManager(Author.class);

        DataSet<Author> all = DataSetBuilder.create(Author.class).store(manager.getAll()).manager(manager).build();

        assert all.size()==2;

    }

}