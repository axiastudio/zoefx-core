package com.axiastudio.zoefx.core.report;

import java.util.*;

/**
 * User: tiziano
 * Date: 05/09/14
 * Time: 11:57
 */
public class Reports {

    private static Map<Class, ArrayList<ReportTemplate>> templates = new HashMap<>();

    public static synchronized void addReportTemplate(Class klass, ReportTemplate template){
        if( !templates.keySet().contains(klass) ){
            templates.put(klass, new ArrayList<>());
        }
        templates.get(klass).add(template);
    }

    public static Optional<List<ReportTemplate>> getTemplates(Class klass){
        if( templates.containsKey(klass) ) {
            return Optional.of(templates.get(klass));
        }
        return Optional.empty();
    }

}
