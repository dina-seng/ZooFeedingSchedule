package com.zoo.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDatabaseConnection {
    private static Connection connection = null; 
    private static final String URL = "jdbc:mysql://localhost:3306/hr";

    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "&&&&";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Connection successfully.");
            } catch (SQLException e) {
                System.out.println("Error establishing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Connection closed successfully.");
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    } 

    public static ResultSet executeQuery(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Execute an update (INSERT, UPDATE, DELETE) query
    public static int executeUpdate(String sql) {
        try {
            Statement statement = getConnection().createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected;
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {
        Connection conn = MySqlDatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connection established successfully.");
        } else {
            System.out.println("Failed to establish connection.");
        }
            String action = "SELECT employee_id, first_name, last_name FROM employees LIMIT 10;";
            ResultSet resultSet = MySqlDatabaseConnection.executeQuery(action);
            try {
                while (resultSet != null && resultSet.next()) {
                    System.out.println(resultSet.getString(1));
                    System.out.println(resultSet.getString(3));
                }
            } catch (SQLException e) {
                System.out.println("Error processing result set: " + e.getMessage());
                e.printStackTrace();
            } finally {
            MySqlDatabaseConnection.closeConnection();
        }
    }
}