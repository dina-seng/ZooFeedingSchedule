package com.zoo.services;

import com.zoo.staff_interface.IStaff;
import com.zoo.models.Animal;
import com.zoo.models.Habitat;
import com.zoo.models.Food;
import java.util.ArrayList;

public class Zoo {
    
    private String zooName;
    
    // Requirement #3: Store all role objects using the Interface type
    private ArrayList<IStaff> users = new ArrayList<>();
    private IStaff loggedInUser; // Tracks who is currently using the system

    // Other data stored in ArrayLists (Week 6 upgrade)
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Habitat> habitats = new ArrayList<>();
    private ArrayList<Food> foods = new ArrayList<>();

    public Zoo(String zooName) {
        this.zooName = zooName;
    }

    // --- LOGIN SYSTEM (Requirement #4) ---
    public void login(String username, String password) {
        for (IStaff user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.loggedInUser = user;
                System.out.println("Login success as " + user.getRole());
                return;
            }
        }
        System.out.println("Login failed: Invalid username or password.");
    }

    public void logout() {
        System.out.println(loggedInUser.getUsername() + " logged out.");
        this.loggedInUser = null;
    }

    // --- PERMISSION CHECKER (Requirement #5) ---
    private boolean requirePermission(String action) {
        if (loggedInUser == null) {
            System.out.println("ERROR: You must login first!");
            return false;
        }
        if (loggedInUser.can(action)) {
            return true;
        } else {
            System.out.println("ACCESS DENIED: Role [" + loggedInUser.getRole() + "] cannot perform: " + action);
            return false;
        }
    }

    // --- ZOO ACTIONS (Using the Permission System) ---

    public void addAnimal(Animal animal) {
        if (requirePermission("CREATE_ITEM")) { // Only Manager
            animals.add(animal);
            System.out.println("Action Success: Animal added.");
        }
    }

    public void removeAnimal(Animal animal) {
        if (requirePermission("DELETE_ITEM")) { // Only Manager
            animals.remove(animal);
            System.out.println("Action Success: Animal removed.");
        }
    }

    public void feedAnimals() {
        if (requirePermission("FEED_ANIMAL")) { // Manager and Keeper
            System.out.println("Action Success: All animals have been fed.");
        }
    }

    public void performMedicalCheck() {
        if (requirePermission("MEDICAL_CHECK")) { // Manager and Vet
            System.out.println("Action Success: Health reports updated.");
        }
    }

    public void viewZooReport() {
        if (requirePermission("VIEW_REPORT")) { // Everyone
            System.out.println("--- Zoo Status Report ---");
            System.out.println("Animals: " + animals.size());
            System.out.println("Habitats: " + habitats.size());
        }
    }

    // Helper method to add users to the system
    public void registerUser(IStaff newUser) {
        users.add(newUser);
    }
}