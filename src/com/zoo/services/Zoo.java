package com.zoo.services;

import com.zoo.interfaces.IHabitat;
import com.zoo.interfaces.IStaff;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.models.staff_roles.Manager;
import com.zoo.models.staff_roles.Staff;
import java.util.ArrayList;
import java.util.List;

public class Zoo {
    public static final String ANIMAL_MANAGE = "ANIMAL_MANAGE";
    public static final String VIEW_REPORT = "VIEW_REPORT";
    public static final String VIEW_HABITAT = "VIEW_HABITAT";
    public static final String CREATE_STAFF = "CREATE_STAFF";
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
        setName(zooName);
        setAddress(address);
        setDefualtManager();
        loggedInUser = null;
        lastMessage = "Zoo Created. Default: admin1 / 12345";
    }

    public String getShopName() { return zooName; }
    public String getAddress() { return address; }
    public String getLastMessage() { return lastMessage; }
    public List<IHabitat> getHabitats() { return habitats; }
    public List<IStaff> getUsers() { return users; }

    public boolean isStaffLoggedIn() { return loggedInUser != null; }
    public IStaff getLoggedInStaff() { return loggedInUser; }

    // -- Set Default Manager  -- //
    private void setDefualtManager() {

        Staff s = new Staff("M001", "Admin", "admin1",  "12345");
        Manager admin = new Manager(s, 1500);
        users.add(admin);
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

    // --- LOGIN SYSTEM (Requirement #4) ---
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
    
    // Create staff
    public void createStaff(String staffId, String fullName, String position,
                            String username, String password) {

        if (!requirePermission(CREATE_STAFF)) return;

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

        if(position.equals("Manager"))
        {
            Staff s = new Staff(staffId, fullName, username, password);
            users.add(new Manager(s,2000));
            setLastMessage("Manager created successfully.");
        }else if(position.equals("Keeper"))
        {
            users.add(new Keeper(staffId, fullName, username, password, 500));
            setLastMessage("Keeper created successfully.");
        }
    }

    // Manage animals
    public void addAnimalToHabitat(Animal animal, IHabitat habitat) {
        if (!requirePermission(ANIMAL_MANAGE)) return;

        if (!canAccessHabitat(habitat)) {
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

        if (!canAccessHabitat(habitat)) {
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

        if (!canAccessHabitat(habitat)) {
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

        if (!canAccessHabitat(habitat)) {
            setLastMessage("You are not assigned to this habitat.");
            return;
        }

        habitat.addSchedule(schedule);
        setLastMessage("Schedule added.");
    }

    public void removeScheduleToHabitat(Schedule schedule, IHabitat habitat) {

        if (!requirePermission(SCHEDULE_MANAGE)) {
            setLastMessage("Permission denied: cannot manage schedule.");
            return;
        }

        if (!canAccessHabitat(habitat)) {
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
        if (!requirePermission(VIEW_HABITAT)) {
            setLastMessage("Permission denied.");
            return;
        }

        if (!canAccessHabitat(habitat)) {
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
        if (loggedInUser instanceof Keeper keeper) {
            return keeper.managesHabitat(habitat);
        }
        return true; 
    }
}