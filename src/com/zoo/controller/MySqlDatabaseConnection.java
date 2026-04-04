package com.zoo.controller;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MySqlDatabaseConnection {
   public static Connection getConnection() throws Exception {
    Properties props = new Properties();
    
    // This method is much smarter: it looks in the 'classpath' (src/bin folders)
    try (InputStream is = MySqlDatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
        if (is == null) {
            throw new FileNotFoundException("Could not find config.properties in the project classpath!");
        }
        props.load(is);
    }

    return DriverManager.getConnection(
        props.getProperty("db.url"),
        props.getProperty("db.user"),
        props.getProperty("db.password")
    );
}
}