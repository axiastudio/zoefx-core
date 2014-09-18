package com.axiastudio.zoefx.core.validators;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tiziano
 * Date: 16/04/14
 * Time: 10:02
 */
public class Validators {

    private static Map<Class, Map<String, Validator>> validators = new HashMap<>();

    public static synchronized void bindValidator(Class klass, String name, Validator validator){
        Map<String, Validator> map = getClassMap(klass);
        map.put(name, validator);
    }

    public static Validator getValidator(Class klass, String name){
        Map<String, Validator> map = getClassMap(klass);
        return map.get(name);
    }

    private static synchronized Map<String, Validator> getClassMap(Class klass){
        Map<String, Validator> map;
        if( validators.keySet().contains(klass) ){
            map = validators.get(klass);
        } else {
            map = new HashMap<String, Validator>();
            validators.put(klass, map);
        }
        return map;
    }
}
