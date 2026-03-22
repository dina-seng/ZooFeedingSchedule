package com.zoo.dao; 

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.*;
import com.zoo.interfaces.IStaff;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.services.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {

    public List<Schedule> getAllSchedules() throws ZooException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.animal_id, s.food_id, s.feeding_time, " +
                     "a.name AS animal_name, f.name AS food_name " +
                     "FROM feeding_schedule s " +
                     "JOIN animal a ON s.animal_id = a.animal_id " +
                     "JOIN food f ON s.food_id = f.food_id";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {

                    Timestamp ts = rs.getTimestamp("feeding_time");
                    

                    String dateStr = ts.toLocalDateTime().toLocalDate().toString();
                    String timeStr = ts.toLocalDateTime().toLocalTime().toString();

                    IStaff tempKeeper = new Keeper("K000", "Unassigned", "temp", "temp", 0.0f);

                    // 4. Call the constructor EXACTLY as you defined it
                    Schedule sch = new Schedule(
                        tempKeeper, // IStaff keeper
                        dateStr,    // String date
                        timeStr     // String time
                    );

                    schedules.add(sch);
                }
        } catch (SQLException e) {
            throw new ZooException("Failed to load schedules: " + e.getMessage());
        }
        return schedules;
    }
}