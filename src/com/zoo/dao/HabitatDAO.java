package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.*;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import com.zoo.models.habitat_types.Forest;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.habitat_types.Ocean;
import com.zoo.models.habitat_types.Savannah;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitatDAO {

    public List<Habitat> getAllHabitats() throws ZooException {
        List<Habitat> habitats = new ArrayList<>();
        String sql = "SELECT h.*, hf.dominant_tree_species, ho.salinity_ppt, hs.grassland_area_sqm " +
                    "FROM habitat h " +
                    "LEFT JOIN habitat_forest hf ON h.habitat_id = hf.habitat_id " +
                    "LEFT JOIN habitat_ocean ho ON h.habitat_id = ho.habitat_id " +
                    "LEFT JOIN habitat_savannah hs ON h.habitat_id = hs.habitat_id";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            // 🔥 STEP 1: Load all food once
            FoodDAO foodDAO = new FoodDAO();
            List<Food> foodList = foodDAO.getInventory();

            // 🔥 STEP 2: Convert to Map for fast lookup
            Map<String, Food> foodMap = new HashMap<>();
            for (Food f : foodList) {
                foodMap.put(f.getName().toLowerCase(), f);
            }

            while (rs.next()) {
                String type = rs.getString("habitat_type");

                // 1. Create the dummy objects required by your protected constructor
                List<Schedule> emptySchedules = new ArrayList<>();

                Habitat h = null; // Declare outside if-else to assign inside

                int habitatId = rs.getInt("habitat_id");
                String name      = rs.getString("name");
                String location  = rs.getString("location");
                int capacity  = rs.getInt("capacity");
                String foodStr = rs.getString("food");

                if (capacity <= 0) {
                    System.err.println("Warning: Skipping habitat '" + name + "' — invalid capacity: " + capacity);
                    continue;
                }

                if (name == null || name.trim().isEmpty()) {
                    System.err.println("Warning: Skipping habitat with empty name.");
                    continue;
                }

                if (location == null || location.trim().isEmpty()) {
                    System.err.println("Warning: Skipping habitat '" + name + "' — missing location.");
                    continue;
                }


                if ("FOREST".equals(type)) {
                    String dominantTree = rs.getString("dominant_tree_species");
                    h = new Forest(emptySchedules, null);
                    // If Forest has setters for its specific fields:
                    // ((Forest) h).setDominantTreeSpecies(dominantTree);

                } else if ("OCEAN".equals(type)) {
                    double salinity = rs.getDouble("salinity_ppt");
                    h = new Ocean(emptySchedules, null);
                    // ((Ocean) h).setSalinity(salinity);

                } else if ("SAVANNAH".equals(type)) {
                    double grassland = rs.getDouble("grassland_area_sqm");
                    h = new Savannah(emptySchedules, null);
                    // ((Savannah) h).setGrasslandArea(grassland);
                } else {
                    System.err.println("Warning: Skipping unknown habitat type: " + type);
                    continue;
                }

                //  THIS WAS THE MISSING PART — set the base fields on every habitat
                h.setId(habitatId);       // make sure Habitat has setId()
                h.setName(name);          // make sure Habitat has setName()
                h.setLocation(location);  // make sure Habitat has setLocation()
                h.setCapacity(capacity);  // make sure Habitat has setCapacity()
                if (foodStr != null && !foodStr.trim().isEmpty()) {
                    Food matchedFood = foodMap.get(foodStr.toLowerCase());

                    if (matchedFood != null) {
                        h.setFood(matchedFood);
                    } else {
                        System.err.println("No matching Food found for: " + foodStr);
                    }
                }

                habitats.add(h);

            }
        } catch (Exception e) {
            throw new ZooException("Database error: " + e.getMessage());
        }
        return habitats;
    }

    // Create new habitat
    public int createHabitat(Habitat h) throws ZooException {
        String sql = "INSERT INTO habitat (name, location, capacity, food, habitat_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, h.getName());
            ps.setString(2, h.getLocation() != null ? h.getLocation() : "Unknown");
            ps.setInt(3, h.getCapacity());
            ps.setString(4, h.getFood() != null ? h.getFood().getName() : null);
            ps.setString(5, h.getClass().getSimpleName().toUpperCase());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    h.setId(id);
                    return id;
                }
            }
        } catch (Exception e) {
            throw new ZooException("Failed to create habitat: " + e.getMessage());
        }
        throw new ZooException("Failed to get generated ID for habitat.");
    }

    // Delete habitat
    public void deleteHabitat(int habitatId) throws ZooException {
        String sql = "DELETE FROM habitat WHERE habitat_id = ?";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, habitatId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ZooException("Failed to delete habitat: " + e.getMessage());
        }
    }

}