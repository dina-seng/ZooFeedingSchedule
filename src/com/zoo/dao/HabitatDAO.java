package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.*;
import com.zoo.models.*;
import com.zoo.models.habitat_types.Forest;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.habitat_types.Ocean;
import com.zoo.models.habitat_types.Savannah;
import com.zoo.services.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            
            while (rs.next()) {
                String type = rs.getString("habitat_type");
                
                // 1. Create the dummy objects required by your protected constructor
                List<Schedule> emptySchedules = new ArrayList<>();
                Food dummyFood = new Food("General Diet", 0.0, "2026-01-01", 0.0);

                Habitat h = null; // Declare outside if-else to assign inside
                
                // Your subclasses call super(feedingTimes, food)
                if ("FOREST".equals(type)) {
                    h = new Forest(emptySchedules, dummyFood);
                } else if ("OCEAN".equals(type)) {
                    h = new Ocean(emptySchedules, dummyFood);
                } else if ("SAVANNAH".equals(type)) {
                    h = new Savannah(emptySchedules, dummyFood);
                }

                
                
                if (h != null) habitats.add(h);
            }
        } catch (SQLException e) {
            throw new ZooException("Database error: " + e.getMessage());
        }
        return habitats;
    }

}