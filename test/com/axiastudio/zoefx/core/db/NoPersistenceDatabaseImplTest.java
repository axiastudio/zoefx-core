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

package com.axiastudio.zoefx.core.db;

import com.axiastudio.zoefx.core.IOC;
import com.axiastudio.zoefx.core.beans.EntityBuilder;
import com.axiastudio.zoefx.demo.Author;
import com.axiastudio.zoefx.desktop.db.DataSet;
import com.axiastudio.zoefx.desktop.db.NoPersistenceDatabaseImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;


public class NoPersistenceDatabaseImplTest {

    @BeforeClass
    public static void setUpClass() throws Exception {

        Database database = new NoPersistenceDatabaseImpl();
        IOC.registerUtility(database, Database.class);
        Manager<Author> manager = database.createManager(Author.class);

        Author lev = EntityBuilder.create(Author.class).set("name", "Lev").set("surname", "Tolstoj").build();
        Author marquez = EntityBuilder.create(Author.class).set("name", "Gabriel García").set("surname", "Márquez").build();

        manager.save(lev);
        manager.save(marquez);

    }

    @Test
    public void testCreateManager() throws Exception {

        Database db = IOC.queryUtility(Database.class);
        Manager<Author> manager = db.createManager(Author.class);

        List<Author> authors = manager.getAll();
        DataSet<Author> dataset = DataSetBuilder.create(Author.class).store(authors).manager(manager).build();

        assert dataset.size()==2;

        Author author = dataset.getStore().get(0);
        assert author.name.equals("Lev");

    }

}