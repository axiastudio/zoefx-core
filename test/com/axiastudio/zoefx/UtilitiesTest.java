package com.axiastudio.zoefx;

import com.axiastudio.zoefx.db.Database;
import com.axiastudio.zoefx.db.JPADatabaseImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * User: tiziano
 * Date: 18/03/14
 * Time: 21:25
 */
public class UtilitiesTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRegisterUtility() throws Exception {
        JPADatabaseImpl db = new JPADatabaseImpl();
        Utilities.registerUtility(db, Database.class);
        Database db2 = Utilities.queryUtility(Database.class);
    }

}
