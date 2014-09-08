package com.axiastudio.zoefx.core.report;

import java.io.File;
import java.util.List;

/**
 * User: tiziano
 * Date: 04/09/14
 * Time: 18:27
 */
public interface ReportEngine {

    public default Boolean canPrint(){ return Boolean.FALSE; };

    public default Boolean canExportToPdf(){ return Boolean.FALSE; }

    public default void toPdf(ReportTemplate template, List store, File file){};

    public default void toPrint(ReportTemplate template, List store, File file){};

}
