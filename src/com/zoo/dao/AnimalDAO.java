package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {
    
        public List<Animal> getAllAnimals() throws ZooException {
            List<Animal> list = new ArrayList<>();
            String sql = "SELECT * FROM animal"; // Make sure table name matches MySQL

            try (Connection conn = MySqlDatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    // Mapping MySQL columns to Java variables
                    String name = rs.getString("name");
                    String species = rs.getString("species");
                    int age = rs.getInt("age");
                    double weight = rs.getDouble("weight");

              
                    // Create the Animal object
                    Animal a = new Animal(name, age, species, weight); 
                    a.setHabitatName(rs.getString("habitat_name"));

                    list.add(a);
                }
            } catch (SQLException e) {
                throw new ZooException("SQL Error in AnimalDAO: " + e.getMessage());
            }
            return list;
        }
}   