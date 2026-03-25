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

        // JOIN habitat so we can resolve habitat_id → habitat name in one query
        String sql = "SELECT a.animal_id, a.name, a.species, a.diet_type, " +
                     "a.weight_kg, a.date_of_birth, a.habitat_id, h.name AS habitat_name " +
                     "FROM animal a " +
                     "LEFT JOIN habitat h ON a.habitat_id = h.habitat_id";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("animal_id");
                String name     = rs.getString("name");
                String species  = rs.getString("species");
                double weightKg = rs.getDouble("weight_kg");
                String dob      = rs.getString("date_of_birth");

                // Calculate age from date_of_birth if your Animal constructor needs it
                int age = 0;
                if (dob != null) {
                    int birthYear = Integer.parseInt(dob.substring(0, 4));
                    age = java.time.Year.now().getValue() - birthYear;
                }

                Animal a = new Animal(name, age, species, weightKg);
                a.setHabitatName(rs.getString("habitat_name"));
                list.add(a);
            }

        } catch (SQLException e) {
            // Print the real error so you can see it during development
            System.err.println("SQL Error in AnimalDAO: " + e.getMessage());
            throw new ZooException("SQL Error in AnimalDAO: " + e.getMessage());
        }
        return list;
    }
}