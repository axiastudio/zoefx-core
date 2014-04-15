package com.axiastudio.zoefx.core.script;

import com.axiastudio.zoefx.core.Utilities;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: tiziano
 * Date: 14/04/14
 * Time: 16:48
 */
public class JSScriptEngineImplTest {

    @BeforeClass
    public static void setUpClass() {
        JSEngineImpl engine = new JSEngineImpl();
        Utilities.registerUtility(engine, ScriptEngine.class);
    }

    @Test
    public void testEval() throws Exception {
        ScriptEngine engine = Utilities.queryUtility(ScriptEngine.class);
        String script = "return \"ciao\";";
        Object eval = engine.eval(script);
        assert eval.toString().equals("ciao");
    }
}
