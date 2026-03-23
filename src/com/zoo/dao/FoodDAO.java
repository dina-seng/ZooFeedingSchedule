package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.*;
import com.zoo.models.Food;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FoodDAO {

    public List<Food> getInventory() throws ZooException {
        List<Food> list = new ArrayList<>();
        String sql = "SELECT food_id, name, food_category, calories_per_kg FROM food";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int    id       = rs.getInt("food_id");
                String name     = rs.getString("name");
                double calories = rs.getDouble("calories_per_kg");

                // Your Food constructor: (name, stock, expiryDate, costPerUnit)
                // Schema has no stock/expiry/cost — use safe defaults
                Food f = new Food(name, 100.0, "2027-12-31", calories);
                f.setId(id);
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // keep this during dev
            throw new ZooException("Food loading failed: " + e.getMessage());
        }
        return list;
    }
}
