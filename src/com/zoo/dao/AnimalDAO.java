package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.InvalidNameException;
import com.zoo.exceptions.OutOfRangeException;
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

                if (name == null || name.trim().isEmpty()) {
                    throw new InvalidNameException("Animal name", "(empty)");
                }
                if (weightKg < 0) {
                    throw new OutOfRangeException("Animal weight cannot be negative: " + weightKg);
                }

                String dob      = rs.getString("date_of_birth");

                // Calculate age from date_of_birth if your Animal constructor needs it
                int age = 0;
                if (dob != null) {
                    int birthYear = Integer.parseInt(dob.substring(0, 4));
                    age = java.time.Year.now().getValue() - birthYear;
                }

                if (age < 0) {
                    throw new OutOfRangeException("Animal age cannot be negative: " + age);
                }

                Animal a = new Animal(name, age, species, weightKg);
                a.setHabitatName(rs.getString("habitat_name"));
                a.setId(id);
                list.add(a);
            }

        } catch (SQLException e) {
            // Print the real error so you can see it during development
            System.err.println("SQL Error in AnimalDAO: " + e.getMessage());
            throw new ZooException("SQL Error in AnimalDAO: " + e.getMessage());
        }
        return list;
    }

    public void addAnimal(Animal animal, String habitatName) throws ZooException {
        String sql = "INSERT INTO animal (name, age, species, weight, habitat_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, animal.getName());
            ps.setInt(2, animal.getAge());
            ps.setString(3, animal.getSpecies());
            ps.setDouble(4, animal.getWeight());
            ps.setString(5, habitatName);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) animal.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new ZooException("Failed to add animal: " + e.getMessage());
        }
    }

    public void deleteAnimal(int animalId) throws ZooException {
        String sql = "DELETE FROM animal WHERE animal_id = ?";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ZooException("Failed to delete animal: " + e.getMessage());
        }
    }
}