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

                if (name == null || name.trim().isEmpty()) {
                    throw new InvalidNameException("Food name", "(empty)");
                }
                if (stock < 0) {
                    throw new OutOfRangeException("Food stock cannot be negative: " + stock);
                }
                if (cost < 0) {
                    throw new OutOfRangeException("Food cost cannot be negative: " + cost);
                }

                Food f = new Food(name, stock, expiry, cost);
                f.setId(id);
                list.add(f);
            }

        } catch (Exception e) {
            throw new ZooException("Failed to load inventory: " + e.getMessage());
        }
        return list;
    }

    public Food addFood(Food food) throws ZooException {
        String sql = "INSERT INTO food (name, stock, expiry_date, costPerUnit) VALUES (?, ?, ?, ?)";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, food.getName());
            ps.setDouble(2, food.getStock());
            ps.setString(3, food.getExpiryDate());
            ps.setDouble(4, food.getCostPerUnit());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) food.setId(rs.getInt(1));
            }
            return food;
        } catch (Exception e) {
            throw new ZooException("Failed to add food: " + e.getMessage());
        }
    }

    public void deleteFood(int foodId) throws ZooException {
        String sql = "DELETE FROM food WHERE food_id = ?";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, foodId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ZooException("Failed to delete food: " + e.getMessage());
        }
    }
}
