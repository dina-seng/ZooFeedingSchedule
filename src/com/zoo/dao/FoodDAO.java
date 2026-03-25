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
        String sql = "SELECT food_id, name, stock, expiry_date, costPerUnit FROM food";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int    id       = rs.getInt("food_id");
                String name     = rs.getString("name");
                int    stock    = rs.getInt("stock");
                String expiry   = rs.getString("expiry_date");
                double cost     = rs.getDouble("costPerUnit");

                Food f = new Food(name, stock, expiry, cost);
                f.setId(id);
                list.add(f);
            }

        } catch (SQLException e) {
            throw new ZooException("Food loading failed: " + e.getMessage());
        }
        return list;
    }
}
