package com.axiastudio.zoefx.core.report;

import java.util.List;

/**
 * User: tiziano
 * Date: 04/09/14
 * Time: 18:27
 */
public interface ReportEngine {

    public void printTemplate(ReportTemplate template, List store);
}
