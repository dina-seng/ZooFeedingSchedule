package com.zoo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDatabaseConnection {
    // Update these to match your MySQL Workbench settings
    private static final String URL = "jdbc:mysql://localhost:3306/zoofeedingschedule";
    private static final String USER = "YOUR_USERNAME"; 
    private static final String PASSWORD = "YOUR_PASSWORD"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found: " + e.getMessage());
        }
    }
}