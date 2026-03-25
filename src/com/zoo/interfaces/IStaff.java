package com.zoo.interfaces;

import com.zoo.models.habitat_types.Habitat;

public interface IStaff {
    int getId();
    String getUsername();
    String getName();
    boolean checkPassword(String input);
    boolean isActive();
    
    boolean can(String action);
    void assignHabitat(Habitat habitat);
}