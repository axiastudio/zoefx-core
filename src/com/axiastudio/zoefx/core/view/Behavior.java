package com.axiastudio.zoefx.core.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: tiziano
 * Date: 20/05/14
 * Time: 14:56
 */
public class Behavior {

    private Properties properties= new Properties();
    private String windowTitle = "";

    public Behavior(InputStream stream) {
        try {
            properties.load(stream);
            parseProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseProperties(Properties properties){
        windowTitle = properties.getProperty("title");
    }

    public Properties getProperties() {
        return properties;
    }

    public String getWindowTitle() {
        return windowTitle;
    }


}
