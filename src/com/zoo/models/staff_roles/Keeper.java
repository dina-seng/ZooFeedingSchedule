package com.zoo.models.staff_roles;

import com.zoo.staff_interface.IStaff;

public class Keeper implements IStaff {
    private String id, name, username, password;

    public static final String ROLE = "Keeper";
    public static final String FEED_ANIMAL = "FEED_ANIMAL";
    public static final String VIEW_HABITAT = "VIEW_HABITAT";

    public Keeper(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override public String getId() { return id; }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public String getRole() { return "Keeper"; }

    @Override
    public boolean can(String action) {
        // Keeper can only feed or view
        return action.equals(FEED_ANIMAL) || action.equals(VIEW_HABITAT);
    }
}

