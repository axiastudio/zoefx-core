package com.axiastudio.zoefx.core.script;

import javax.script.Bindings;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

/**
 * User: tiziano
 * Date: 14/04/14
 * Time: 16:19
 */
public class JSEngineImpl implements ScriptEngine {

    @Override
    public Object eval(String script) {
        return eval(script, null);
    }

    @Override
    public Object eval(String script, Map<String, Object> map) {

        String pre = "function f(){ ";
        String post = " } f()";

        ScriptEngineManager manager = new ScriptEngineManager();
        manager.put("global", "global bindings");

        javax.script.ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.put("engine", "engine bindings");

        Bindings bindings = engine.createBindings();
        if( map != null ) {
            for( String key: map.keySet() ) {
                bindings.put(key, map.get(key));
            }
        }
        try {
            Object eval = engine.eval(pre + script + post, bindings);
            return eval;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}
