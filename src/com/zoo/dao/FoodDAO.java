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
    // Double check your table name in MySQL! Is it 'food' or 'food_inventory'?
    String sql = "SELECT * FROM food"; 

    try (Connection conn = MySqlDatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            // Use the column names from your MySQL table
            int id = rs.getInt("food_id");
            String name = rs.getString("food_name");
            double stock = rs.getDouble("stock_quantity");
            String expiry = rs.getString("expiry_date");
            double cost = rs.getDouble("unit_cost");

            Food f = new Food(name, stock, expiry, cost);
            f.setId(id); // Set the ID from DB
            list.add(f);
        }
    } catch (SQLException e) {
        throw new ZooException("Food loading failed: " + e.getMessage());
    }
    return list;
}
}