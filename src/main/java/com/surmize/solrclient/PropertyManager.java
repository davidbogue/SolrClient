package com.surmize.solrclient;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {

    private static Properties properties;
    
    private PropertyManager() {
    }
    
    private synchronized static void initialize() {
        try {
            properties = new Properties();
            properties.load(PropertyManager.class.getResourceAsStream("/solrserver.properties"));
        } catch (IOException ex) {
            Logger.getLogger(PropertyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static String getSetting(String key, String defaultValue) {
    	if( propertyFile() == null ) {
    		initialize();
    	}
        return propertyFile().getProperty(key, defaultValue);
    }

    public static String getSetting(String key) {
        return PropertyManager.getSetting(key, null);
    }
    
    public static Integer getIntegerSetting(String key) {
        String prop = PropertyManager.getSetting(key, "-1" );
        return Integer.parseInt( prop );
    }
    
   private static Properties propertyFile(){
    	return properties;
    }
    
    public static void reload() {
    	initialize();
    }
}
