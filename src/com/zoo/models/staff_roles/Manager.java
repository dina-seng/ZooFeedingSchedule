package com.zoo.models.staff_roles;

import com.zoo.staff_interface.IStaff;

public class Manager implements IStaff {
    private String id, name, username, password;
 
    public static final String ROLE = "Manager";
    public static final String ADD_ANIMAL = "ADD_ANIMAL";
    public static final String REMOVE_ANIMAL = "REMOVE_ANIMAL";
    public static final String VIEW_REPORT = "VIEW_REPORT";

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
        if(action.equals(ADD_ANIMAL) || 
           action.equals(REMOVE_ANIMAL) || 
           action.equals(VIEW_REPORT)) {
            return true;
        }   
        return false; 
    }
}
