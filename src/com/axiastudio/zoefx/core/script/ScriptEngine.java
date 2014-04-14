package com.axiastudio.zoefx.core.script;

import java.util.Map;

/**
 * User: tiziano
 * Date: 14/04/14
 * Time: 16:15
 */
public interface ScriptEngine {

    public Object eval(String script);

    public Object eval(String script, Map<String, Object> map);

}
