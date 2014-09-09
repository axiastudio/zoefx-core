package com.axiastudio.zoefx.core.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 05/09/14
 * Time: 11:57
 */
public class Reports {

    private static Map<Class, ArrayList<ReportTemplate>> templates = new HashMap<>();

    public static void addReportTemplate(Class klass, ReportTemplate template){
        if( !templates.keySet().contains(klass) ){
            templates.put(klass, new ArrayList<>());
        }
        templates.get(klass).add(template);
    }

    public static List<ReportTemplate> getTemplates(Class klass){
        return templates.get(klass);
    }

}
