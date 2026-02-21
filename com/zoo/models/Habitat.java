package com.zoo.models;

import com.zoo.services.Schedule;
import com.zoo.staff_interface.IStaff; 
import java.util.ArrayList;     
import java.util.Objects;

public class Habitat {
    private String id;
    private String species;
    private Food food;
    private double amountFood;
    private Schedule feedingTime;
    
    // Requirement Upgrade: Use ArrayList instead of Animal[]
    private ArrayList<Animal> animals; 

    public Habitat(String id, String species, double amountFood, Schedule feedingTime, Food food) {
        this.id = id;
        this.species = species;
        this.amountFood = amountFood;
        this.feedingTime = feedingTime;
        this.food = food;
        this.animals = new ArrayList<>(); // Initialize the list
    }

    // Requirement #5: Updated to use IStaff and permission check
    public void setFeedingTime(Schedule feedingTime, IStaff s) {
        // Instead of hardcoded "Manager" string, we use the permission method
        if (s.can("UPDATE_SCHEDULE")) {
            this.feedingTime = feedingTime;
            System.out.println("Schedule updated successfully.");
        } else {
            System.out.println("Access Denied: " + s.getRole() + " cannot set feeding times.");
        }
    }

    // Method to add animal to habitat using ArrayList
    public void addAnimal(Animal a) {
        if(!animals.contains(a)) {
            animals.add(a);
        }
    }

    public ArrayList<Animal> getAnimals() { return animals; }
    public String getHabitatID() { return id; }
}