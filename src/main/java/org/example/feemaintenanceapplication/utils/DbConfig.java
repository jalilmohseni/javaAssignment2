package org.example.feemaintenanceapplication.utils;


import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    //object of class Property to access any *.properties file
    private static Properties prop = new Properties();
    //crearte static block of code
    static {
        //reading the db.properties file
        try(InputStream in = DbConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties file not found");
            }
            //loads the input stream from the .properties files into Properties object
            prop.load(in);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading properties file", e);
        }
    }

    //read from properties object
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
