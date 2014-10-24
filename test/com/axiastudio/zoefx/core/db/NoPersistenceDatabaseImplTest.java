package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.Utilities;
import com.axiastudio.zoefx.core.model.EntityBuilder;
import com.axiastudio.zoefx.demo.Author;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;


public class NoPersistenceDatabaseImplTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Database database = new NoPersistenceDatabaseImpl();
        Utilities.registerUtility(database, Database.class);
        Manager<Author> manager = database.createManager(Author.class);

        Author lev = EntityBuilder.create(Author.class).set("name", "Lev").set("surname", "Tolstoj").build();
        Author marquez = EntityBuilder.create(Author.class).set("name", "Gabriel García").set("surname", "Márquez").build();

        manager.commit(lev);
        manager.commit(marquez);

    }

    @Test
    public void testCreateManager() throws Exception {

        Database db = Utilities.queryUtility(Database.class);
        Manager<Author> manager = db.createManager(Author.class);

        List<Author> authors = manager.getAll();
        DataSet<Author> dataset = DataSetBuilder.create(Author.class).store(authors).manager(manager).build();

        assert dataset.size()==2;

        Author author = dataset.getStore().get(0);
        assert author.name.equals("Lev");

    }

}