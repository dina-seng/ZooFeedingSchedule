package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.ZooException;
import com.zoo.interfaces.IStaff;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.services.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public List<Schedule> getAllSchedules() throws ZooException {
        List<Schedule> schedules = new ArrayList<>();

        String sql = "SELECT fs.schedule_id, fs.feeding_time, fs.day_of_week, " +
                     "fs.quantity_kg, fs.notes, " +
                     "a.name AS animal_name, " +
                     "f.name AS food_name, " +
                     "CONCAT(st.first_name, ' ', st.last_name) AS keeper_name, " +
                     "st.staff_id, st.email " +
                     "FROM feeding_schedule fs " +
                     "JOIN animal a  ON fs.animal_id = a.animal_id " +
                     "JOIN food f    ON fs.food_id   = f.food_id " +
                     "JOIN staff st  ON fs.staff_id  = st.staff_id";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
              
                String timeStr   = rs.getString("feeding_time");   // "08:00:00"
                String dayOfWeek = rs.getString("day_of_week");    // "MONDAY"
                String animalName = rs.getString("animal_name");
                String foodName   = rs.getString("food_name");
                String keeperName = rs.getString("keeper_name");
                String keeperEmail = rs.getString("email");
                String staffId    = String.valueOf(rs.getInt("staff_id"));

                // Build a real Keeper from DB data instead of a dummy
                IStaff keeper = new Keeper(staffId, keeperName, keeperEmail, "hidden", 0.0f);

                Schedule sch = new Schedule(keeper, dayOfWeek, timeStr);

                schedules.add(sch);

                // Debug — remove once confirmed working
                System.out.println("Loaded schedule: " + animalName + 
                                   " | " + foodName + 
                                   " | " + dayOfWeek + " " + timeStr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ZooException("Failed to load schedules: " + e.getMessage());
        }
        return schedules;
    }
}