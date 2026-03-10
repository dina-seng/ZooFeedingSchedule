package com.zoo.services;

import com.zoo.interfaces.IHabitat;
import com.zoo.interfaces.IStaff;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.habitat_types.Forest;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.habitat_types.Ocean;
import com.zoo.models.habitat_types.Savannah;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.models.staff_roles.Manager;
import java.util.ArrayList;
import java.util.List;

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
    private List<IStaff> users = new ArrayList<>();
    private List<IHabitat> habitats = new ArrayList<>();
    private List<Food> foodInventory = new ArrayList<>();

    private IStaff loggedInUser; // Current user tracking

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
    public List<IHabitat> getHabitats() { return habitats; }
    public List<IStaff> getUsers() { return users; }

    public boolean isStaffLoggedIn() { return loggedInUser != null; }
    public IStaff getLoggedInStaff() { return loggedInUser; }

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

        for (IStaff user : users) {
            if (user.getUsername().equalsIgnoreCase(username.trim())) {

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
                return;
            }
        }
        setLastMessage("Login failed: Invalid username or password.");
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
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equalsIgnoreCase(username.trim())) {
                setLastMessage("Cannot create staff: username already exists.");
                return;
            }
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
    public void addAnimalToHabitat(Animal animal, IHabitat habitat) {
        if (!requirePermission(ANIMAL_MANAGE)) return;

        if (canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }
        // animal added
        habitat.addAnimal(animal);
        setLastMessage(animal.getName() + " added to " + habitat.getName());
    }

    public void removeAnimalFromHabitat(Animal animal, IHabitat habitat) {
        if (!requirePermission(ANIMAL_MANAGE)) {
            setLastMessage("Permission denied: cannot remove animal.");
            return;
        }

        if (canAccessHabitat(habitat)) {
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

    public void feedHabitat(IHabitat habitat, int foodId, double amount) {
        if (!requirePermission(FOOD_MANAGE)) return;

        if (canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }
        Food food = null;
        for (Food f : foodInventory) {
            if (f.getId() == foodId) {
                food = f;
                break;
            }
        }
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
    public void addScheduleToHabitat(Schedule schedule, IHabitat habitat) {
        if (!requirePermission(SCHEDULE_MANAGE)) {
            setLastMessage("Permission denied.");
            return;
        }

        if (canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        habitat.addSchedule(schedule);
        setLastMessage("Schedule added.");
    }

    public void removeScheduleFromHabitat(Schedule schedule, IHabitat habitat) {

        if (!requirePermission(SCHEDULE_MANAGE)) {
            setLastMessage("Permission denied: cannot manage schedule.");
            return;
        }

        if (canAccessHabitat(habitat)) {
            setLastMessage("Permission denied: not your habitat.");
            return;
        }

        habitat.removeSchedule(schedule);
        setLastMessage("Schedule removed from " + habitat.getName());
    }

    // Overall information
    public void viewZooReport() {
        if (requirePermission(VIEW_REPORT)) {
            System.out.println("--- Zoo Status Report ---");
            System.out.println("Animals: " + countAllAnimals(habitats));
            System.out.println("Habitats: " + habitats.size());
            System.out.println("Foods: " + foodInventory.size());
        }
    }

    public void viewHabitatSchedules(IHabitat habitat) {
        if (!requirePermission(HABITAT_MANAGE)) {
            setLastMessage("Permission denied.");
            return;
        }

        if (canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        System.out.println(habitat.getName() + ".schedules:");
        System.out.println("----------------------------------");

        int index = 1;
        for (Schedule s : habitat.getFeedingTimes()) {
            System.out.println(index + ". " + s);
            index++;
        }
    }

    // Manage habitats
    public void createHabitat(String type, Food food) {
        if (!requirePermission(HABITAT_MANAGE)) return;

        Habitat h = defineHabitats(type, food);
        if(h != null) habitats.add(h);
        setLastMessage("Habitat created successfully.");
    }

    public void assignHabitatToKeeper(String staffId, IHabitat habitat) {
        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId)) {
            setLastMessage("Staff ID cannot be empty.");
            return;
        }

        for (IStaff staff : users) {
            if (staff.getId().equalsIgnoreCase(staffId.trim())) {
                try {
                    staff.assignHabitat(habitat);
                    setLastMessage("Habitat '" + habitat.getName() + "' assigned to '" + staff.getUsername() + "'.");
                } catch (UnsupportedOperationException e) {
                    setLastMessage(staff.getUsername() + " cannot be assigned habitats.");
                }
                return;
            }
        }
        setLastMessage("Staff not found.");
    }

    // Helper methods
    public void registerUser(IStaff newUser) {
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

    private int countAllAnimals(List<IHabitat> habitats) {
        int count = 0;
        for (IHabitat h : habitats) {
            count += h.getAnimals().size();
        }
        return count;
    }

    private boolean canAccessHabitat(IHabitat habitat) {
        return !loggedInUser.canAccessHabitat(habitat);
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