package com.axiastudio.zoefx.core.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * User: tiziano
 * Date: 09/06/14
 * Time: 10:48
 */
public class Comparators {

    public static Comparator<String> DateComparator = (t, t1) -> {
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            Date d1 = dateFormat.parse(t);
            Date d2 = dateFormat.parse(t1);
            return Long.compare(d1.getTime(), d2.getTime());
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return -1;
    };

}
