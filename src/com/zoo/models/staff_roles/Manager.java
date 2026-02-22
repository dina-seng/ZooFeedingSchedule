package com.zoo.models.staff_roles;

import com.zoo.staff_interface.IStaff;

public class Manager implements IStaff {
    private String id, name, username, password;
    
    public Manager(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override public String getId() { return id; }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public String getRole() { return "Manager"; }

    @Override
    public boolean can(String action) {
        // Manager can do everything!
        return true; 
    }
}
