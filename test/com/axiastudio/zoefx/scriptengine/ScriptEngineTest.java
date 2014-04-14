package com.axiastudio.zoefx.scriptengine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Date;

/**
 * User: tiziano
 * Date: 14/04/14
 * Time: 14:45
 */
public class ScriptEngineTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("global", "global bindings");

        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.put("engine", "engine bindings");

        Bindings bindings = engine.createBindings();
        String aString = "This is a test.";
        bindings.put("obj", aString);

        String pre = "function test(obj){ ";
        String post = " } test(obj)";

        String validation = "return obj.length() == 15";

        String code = pre + validation + post;

        Object eval = engine.eval(code, bindings);

        System.out.println(eval);

    }
}
