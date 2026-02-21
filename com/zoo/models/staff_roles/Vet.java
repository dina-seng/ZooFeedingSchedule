package com.zoo.models.staff_roles;

import com.zoo.staff_interface.IStaff;

public class Vet implements IStaff {
    private String id;
    private String name;
    private String username;
    private String password;

    public Vet(String id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Override public String getId() { return id; }
    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public String getRole() { return "VET"; }

    @Override
    public boolean can(String action) {
        // Vets can check animals and view the zoo, but can't manage staff or habitats
        return action.equals("MEDICAL_CHECK") || 
               action.equals("VIEW_ZOO") || 
               action.equals("GIVE_MEDICINE");
    }
}