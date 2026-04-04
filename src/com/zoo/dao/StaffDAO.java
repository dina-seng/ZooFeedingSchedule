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

    // Java now sees that MySqlDatabaseConnection.getConnection() might throw 'Exception'
    try (Connection conn = MySqlDatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

       while (rs.next()) {
            
            int id = rs.getInt("staff_id");
            String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
            String user = rs.getString("email");
            String pass = rs.getString("password");
            float sal = rs.getFloat("salary"); 

            Staff member = "MANAGER".equals(rs.getString("staff_type")) ? 
                new Manager(id, fullName, user, pass, sal) : 
                new Keeper(id, fullName, user, pass, sal);
            
            staffList.add(member);
        }
    } catch (Exception e) { // CHANGED THIS FROM SQLException TO Exception
        throw new ZooException("Failed to load staff: " + e.getMessage());
    }
    return staffList;
}

public Staff login(String email, String password) throws ZooException {
    String sql = "SELECT * FROM staff WHERE email = ? AND password = ? AND is_active = TRUE";

    try (Connection conn = MySqlDatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                int id = rs.getInt("staff_id");
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String type = rs.getString("staff_type");
                float salary = rs.getFloat("salary");

                return "MANAGER".equalsIgnoreCase(type) ? 
                    new Manager(id, fullName, email, password, salary) : 
                    new Keeper(id, fullName, email, password, salary);
            }
        }
    } catch (Exception e) { // CHANGED THIS FROM SQLException TO Exception
        throw new ZooException("Database login error: " + e.getMessage());
    }
    throw new AuthenticationException("Login failed: email or password is incorrect.");
}
}