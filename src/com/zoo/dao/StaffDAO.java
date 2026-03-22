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
                String id = String.valueOf(rs.getInt("staff_id"));
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
                String id = String.valueOf(rs.getInt("staff_id")); // Will be "98" or "99"
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
    
}