package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.ZooException;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.models.staff_roles.Staff;
import com.zoo.models.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public List<Schedule> getAllSchedules() throws ZooException {
        List<Schedule> schedules = new ArrayList<>();

        // Select all required fields
        String sql = "SELECT fs.schedule_id, fs.animal_id, fs.staff_id, fs.food_id, " +
                "fs.feeding_time, fs.quantity_kg, fs.notes, fs.completed, " +
                "st.first_name, st.last_name, st.email " +
                "FROM feeding_schedule fs " +
                "JOIN staff st ON fs.staff_id = st.staff_id";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                int scheduleId = rs.getInt("schedule_id");
                int animalId   = rs.getInt("animal_id");
                int staffId    = rs.getInt("staff_id");
                int foodId     = rs.getInt("food_id");
                String feedingTime = rs.getString("feeding_time");
                float quantityKg   = rs.getFloat("quantity_kg");
                String notes       = rs.getString("notes");
                boolean completed  = rs.getBoolean("completed");

                // Build Keeper object
                String keeperName  = rs.getString("first_name") + " " + rs.getString("last_name");
                String keeperEmail = rs.getString("email");
                Staff keeper = new Keeper(staffId, keeperEmail, "12345678", "12345678", 500.0f);

                // Build Schedule object
                Schedule sch = new Schedule(scheduleId, animalId, staffId, foodId,
                        feedingTime, quantityKg, notes, completed);

                schedules.add(sch);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ZooException("Failed to load schedules: " + e.getMessage());
        }

        return schedules;
    }

    // Optional: method to update completion status
    public static void updateCompletionStatus(int scheduleId, boolean completed) throws SQLException {
        String sql = "UPDATE feeding_schedule SET completed = ? WHERE schedule_id = ?";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, completed);
            stmt.setInt(2, scheduleId);
            stmt.executeUpdate();
        }
    }
}