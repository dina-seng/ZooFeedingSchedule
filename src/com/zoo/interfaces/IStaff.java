package com.zoo.interfaces;

public interface IStaff {
    String getId();
    String getUsername();
    String getName();
    boolean checkPassword(String input);
    boolean isActive();
    
    boolean can(String action);
    boolean canAccessHabitat(IHabitat habitat);
    void assignHabitat(IHabitat habitat);
}