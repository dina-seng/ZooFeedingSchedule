package com.zoo.dao;

import com.zoo.controller.MySqlDatabaseConnection;
import com.zoo.exceptions.*;
import com.zoo.models.staff_roles.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class StaffDAO {

    public List<Staff> getAllStaff() throws ZooException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

           while (rs.next()) {
                String type = rs.getString("staff_type"); // MANAGER or KEEPER 

                
                
                // Mapping SQL columns to your constructor: (id, name, username, password, salary)
                int id = rs.getInt("staff_id");
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String user = rs.getString("email"); // Using email as username
                String pass = rs.getString("password");
                float sal = rs.getFloat("salary"); 

                if (pass == null || pass.trim().isEmpty()) {
                    pass = "12345678"; 
                }

                Staff member;
                if ("MANAGER".equals(type)) {
                    member = new Manager(id, fullName, user, pass, sal);
                } else {
                    member = new Keeper(id, fullName, user, pass, sal);
                }
                staffList.add(member);
            }
        } catch (SQLException e) {
            throw new ZooException("Failed to load staff: " + e.getMessage());
        }
        return staffList;
    }


  public Staff login(String email, String password) throws ZooException {
    String sql = "SELECT * FROM staff WHERE email = ? AND password = ? AND is_active = TRUE";

    // try-with-resources ensures the connection closes ONLY after this method finishes
    try (Connection conn = MySqlDatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                // Mapping the data you just inserted
                int id = rs.getInt("staff_id"); // Will be "98" or "99"
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("staff_type");
                float salary = rs.getFloat("salary");

                if ("MANAGER".equalsIgnoreCase(type)) {
                    return new Manager(id, fullName, email, password, salary);
                } else {
                    return new Keeper(id, fullName, email, password, salary);
                }
            }
        }
    } catch (SQLException e) {
        throw new ZooException("Database login error: " + e.getMessage());
    }
    return null; // Means no match found
}
    public Staff createStaff(String fullName, String position, String email, String password, float salary) throws ZooException {
        String[] parts = fullName.trim().split(" ", 2);
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";

        String sql = "INSERT INTO staff (first_name, last_name, email, password, salary, staff_type, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, TRUE)";

        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setFloat(5, salary);
            pstmt.setString(6, position.toUpperCase());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    if (position.equalsIgnoreCase("Manager")) {
                        return new Manager(id, fullName, email, password, salary);
                    } else {
                        return new Keeper(id, fullName, email, password, salary);
                    }
                }
            }

        } catch (SQLException e) {
            throw new ZooException("Failed to create staff: " + e.getMessage());
        }
        throw new ZooException("Failed to retrieve generated ID.");
    }

    public void deleteStaff(int staffId) throws ZooException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        try (Connection conn = MySqlDatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new ZooException("Failed to delete staff: " + e.getMessage());
        }
    }

}