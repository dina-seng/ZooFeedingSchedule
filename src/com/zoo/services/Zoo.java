package com.zoo.services;

import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.habitat_types.*;
import com.zoo.models.staff_roles.*;
import java.util.ArrayList;
import java.util.List;


// interface for report generation before Java 8, now replaced by functional interface and lambda expression
// interface ZooReport {
//     void generateReport();
// }

@FunctionalInterface
interface ZooReport {
    void generateReport();
}

public class Zoo {
    public static final String ANIMAL_MANAGE = "ANIMAL_MANAGE";
    public static final String VIEW_REPORT = "VIEW_REPORT";
    public static final String STAFF_MANAGE = "STAFF_MANAGE";
    public static final String HABITAT_MANAGE = "HABITAT_MANAGE";
    public static final String SCHEDULE_MANAGE = "SCHEDULE_MANAGE";
    public static final String FOOD_MANAGE = "FOOD_MANAGE";

    // Information
    private String zooName;
    private String address;

    // Alert message
    private String lastMessage;

    // Resources
    private List<Staff> users = new ArrayList<>();
    private List<Habitat> habitats = new ArrayList<>();
    private List<Food> foodInventory = new ArrayList<>();

    private Staff loggedInUser; // Current user tracking

    // -- Constructor -- //
    public Zoo(String zooName, String address) {
        this.zooName = zooName.trim();
        this.address = address.trim();
        setDefaultManager();
        loggedInUser = null;
        lastMessage = "Zoo Created. Default: admin1 / 12345";
    }

    public String getZooName() { return zooName; }
    public String getAddress() { return address; }
    public String getLastMessage() { return lastMessage; }
    public List<Habitat> getHabitats() { return habitats; }
    public List<Staff> getUsers() { return users; }

    public boolean isStaffLoggedIn() { return loggedInUser != null; }
    public Staff getLoggedInStaff() { return loggedInUser; }

    // Set Default Manager
    private void setDefaultManager() {
        Manager admin = new Manager("M001", "Admin", "admin1",  "12345678", 1500);
        Keeper k = new Keeper("K001", "Keeper", "keeper1",  "12345678", 1500);
        registerUser(admin);
        registerUser(k);
    }

    public void setName(String name) {
        if(isBlank(name)) this.zooName = "Unknown";
        else this.zooName = name.trim();
    }

    public void setAddress(String address) {
        if(isBlank(address)) this.address = "Unknown";
        else this.address = address.trim();
    }

    // LOGIN CHECK (dependency)
    private boolean requireStaffLogin() {
        if (loggedInUser == null) {
            setLastMessage("Action denied: staff must login first.");
            return false;
        }

        if (!loggedInUser.isActive()) {
            loggedInUser = null;
            setLastMessage("Action denied: staff is inactive (auto logout).");
            return false;
        }

        return true;
    }

    // --- LOGIN SYSTEM ---
    public void login(String username, String password) {

        if (isBlank(username) || password == null) {
            setLastMessage("Login failed: missing username/password.");
            return;
        }

        users.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username.trim()))
            .findFirst()
            .ifPresentOrElse(user -> {

                if (!user.isActive()) {
                    setLastMessage("Login failed: staff is inactive.");
                    return;
                }

                if (!user.checkPassword(password)) {
                    setLastMessage("Login failed: wrong password.");
                    return;
                }

                loggedInUser = user;
                setLastMessage("Login success. Welcome " + user.getName() + "!");
                
                }, () -> setLastMessage("Login failed: username not found."));
        }

    public void logout() {
        if (loggedInUser != null) {
            String name = loggedInUser.getUsername();
            loggedInUser = null;
            setLastMessage(name + " logged out.");
        }
    }
    
    // Manage staff
    public void createStaff(String staffId, String fullName, String position,
                String username, String password, float salary) {

        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId) || isBlank(username)) {
            setLastMessage("Cannot create staff: staffId/username is empty.");
            return;
        }

        // duplicate username check
        boolean exists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username.trim()));

        if (exists) {
            setLastMessage("Cannot create staff: username already exists.");
            return;
        }

        if(position.equalsIgnoreCase("Manager"))
        {
            users.add(new Manager(staffId, fullName, username, password, salary));
            setLastMessage("Manager created successfully.");
        }else if(position.equalsIgnoreCase("Keeper"))
        {
            users.add(new Keeper(staffId, fullName, username, password, salary));
            setLastMessage("Keeper created successfully.");
        }
    }

    public void removeStaff(String staffId) {

        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId)) {
            setLastMessage("staffId is empty.");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(staffId.trim())) {
                String name = users.get(i).getUsername();
                users.remove(i);
                setLastMessage("Staff " + name + " removed successfully.");
                return;
            }
        }
        setLastMessage("Staff not found.");
    }

    // Manage animals
    public void addAnimalToHabitat(Animal animal, Habitat habitat) {
        if (!requirePermission(ANIMAL_MANAGE)) return;

        if (!getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }
        // animal added
        habitat.addAnimal(animal);
        setLastMessage(animal.getName() + " added to " + habitat.getName());
    }

    public void removeAnimalFromHabitat(Animal animal, Habitat habitat) {
        if (!requirePermission(ANIMAL_MANAGE)) {
            setLastMessage("Permission denied: cannot remove animal.");
            return;
        }

        if (getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }
        // animal removed
        habitat.removeAnimal(animal);
        setLastMessage(animal.getName() + " removed from " + habitat.getName());
    }

    // Manage foods
    public void addFoodToInventory(Food food) {
        if (!requirePermission(FOOD_MANAGE)) {
            setLastMessage("Permission denied.");
            return;
        }

        foodInventory.add(food);
        setLastMessage("Food added to central inventory.");
    }

    public void feedHabitat(Habitat habitat, int foodId, double amount) {
        if (!requirePermission(FOOD_MANAGE)) return;

        if (getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        // Find food in inventory using lamda expression
        Food food = foodInventory.stream()
                .filter(f -> f.getId() == foodId)
                .findFirst()
                .orElse(null);
            
        if (food == null) {
            setLastMessage("Food not found.");
            return;
        }
        if (food.getStock() < amount) {
            setLastMessage("Not enough food in storage.");
            return;
        }

        // Deduct from central inventory
        food.setStock(food.getStock() - amount);
        setLastMessage("Habitat fed successfully.");
    }

    // Manage schedule
    public void addScheduleToHabitat(Schedule schedule, Habitat habitat) {
        if (!requirePermission(SCHEDULE_MANAGE)) {
            setLastMessage("Permission denied.");
            return;
        }

        if (getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        habitat.addSchedule(schedule);
        setLastMessage("Schedule added.");
    }

    public void removeScheduleFromHabitat(Schedule schedule, Habitat habitat) {

        if (!requirePermission(SCHEDULE_MANAGE)) {
            setLastMessage("Permission denied: cannot manage schedule.");
            return;
        }

        if (getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("Permission denied: not your habitat.");
            return;
        }

        habitat.removeSchedule(schedule);
        setLastMessage("Schedule removed from " + habitat.getName());
    }

    // Overall information
    public void viewZooReport() {
        if (requirePermission(VIEW_REPORT)) {
            
            // Anonymous Inner Class 
            ZooReport oldReport = new ZooReport() {
                @Override
                public void generateReport() {
                    System.out.println("=== Report ===");
                    System.out.println("Total Staff: " + users.size());
                }
            };
            oldReport.generateReport();

            // we can use a lambda expression to implement the functional interface:
            ZooReport summaryReport = () -> {
                System.out.println("--- " + getZooName().toUpperCase() + " SUMMARY REPORT ---");
                System.out.println("Total Staff: " + users.size());
                System.out.println("Active Habitats: " + habitats.size());
                System.out.println("Total Animals: " + countAllAnimals(habitats));
                System.out.println("Generated by: " + getLoggedInStaff().getName());
            };

            summaryReport.generateReport();
        }
    }

    public void viewHabitatSchedules(Habitat habitat) {
        if (!requirePermission(HABITAT_MANAGE)) {
            setLastMessage("Permission denied.");
            return;
        }

        if (getLoggedInStaff().canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        System.out.println(habitat.getName() + ".schedules:");
        System.out.println("----------------------------------");

        habitat.getFeedingTimes().forEach(s -> System.out.println(s));
    }

    // Manage habitats
    public void createHabitat(String type, Food food) {
        if (!requirePermission(HABITAT_MANAGE)) return;

        Habitat h = defineHabitats(type, food);
        if(h != null) habitats.add(h);
        setLastMessage("Habitat created successfully.");
    }

    public void assignHabitatToKeeper(String staffId, Habitat habitat) {
        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId)) {
            setLastMessage("Staff ID cannot be empty.");
            return;
        }

    users.stream().filter(s -> s.getId().equalsIgnoreCase(staffId.trim())).findFirst()
        .ifPresentOrElse(staff -> {
            try {
                staff.assignHabitat(habitat);
                setLastMessage("Habitat '" + habitat.getName() + "' assigned to '" + staff.getUsername() + "'.");
            } catch (UnsupportedOperationException e) {
                setLastMessage(staff.getUsername() + " cannot be assigned habitats.");
            }
        }, () -> setLastMessage("Staff not found."));
    }

    // Helper methods
    public void registerUser(Staff newUser) {
        users.add(newUser);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void setLastMessage(String msg) {
        lastMessage = msg;
    }

    private boolean requirePermission(String action) {
        if (!requireStaffLogin()) return false;

        if (!loggedInUser.can(action)) {
            setLastMessage("Permission denied for action: " + action);
            return false;
        }
        return true;
    }

    private int countAllAnimals(List<Habitat> habitats) {
        return habitats.stream()
                .mapToInt(h -> h.getAnimals().size())
                .sum();
    }

    private Habitat defineHabitats(String type,  Food food) {
        Habitat newHabitat = null;
        if (type.equals("forest")) {
            newHabitat = new Forest(null, food);
        } else if (type.equals("ocean")) {
            newHabitat = new Ocean( null, food );
        } else if (type.equals("savannah")) {
            newHabitat = new Savannah(null, food);
        } else {
            setLastMessage("Unknown habitat type. Use: forest, ocean, savannah.");
            return null;
        }
        return newHabitat;
    }

}