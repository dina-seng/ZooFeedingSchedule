package com.zoo.services;


import com.zoo.dao.*;
import com.zoo.exceptions.InvalidHabitatException;
import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.habitat_types.Forest;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.habitat_types.Ocean;
import com.zoo.models.habitat_types.Savannah;
import com.zoo.models.staff_roles.Keeper;
import com.zoo.models.staff_roles.Manager;
import com.zoo.models.staff_roles.Staff;
import java.util.ArrayList;
import java.util.List;

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

    private String zooName;
    private String address;
    private String lastMessage;

    private List<Staff> users = new ArrayList<>();
    private List<Habitat> habitats = new ArrayList<>();
    private List<Food> foodInventory = new ArrayList<>(); 
    private List<Animal> animals = new ArrayList<>(); // Added to store animals in the zoo
    private AnimalDAO animalDAO = new AnimalDAO(); 
    private HabitatDAO habitatDAO = new HabitatDAO();
    private FoodDAO foodDAO = new FoodDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    private Staff loggedInUser;

    public Zoo(String zooName, String address) {
        this.zooName = zooName.trim();
        this.address = address.trim();
        setDefaultManager(); 

        initializeData();

        System.out.println("System: Loaded " + animals.size() + " animals from database.");
    
        loggedInUser = null;
        lastMessage = "Zoo Created. Default: admin1 / 12345";
    } 

    public void initializeData() {
        try {
            this.users = staffDAO.getAllStaff();
            this.foodInventory = foodDAO.getInventory();
            this.habitats = habitatDAO.getAllHabitats();          
            this.animals = animalDAO.getAllAnimals(); 

        for (Animal a : animals) {
            for (Habitat h : habitats) {
                // If the Animal's record says "Forest2" and the Habitat is named "Forest2"
                if (a.getHabitatName() != null && a.getHabitatName().equalsIgnoreCase(h.getName())) {
                    h.getAnimals().add(a); // Add to the specific habitat list
                }
            }
        }
            
            System.out.println("Success: Loaded " + animals.size() + " animals.");
        } catch (ZooException e) {
            System.err.println("Sync Error: " + e.getMessage());
        }
    }



    public List<Animal> getAnimals() {
        return this.animals;
    }
    public List<Food> getFoodInventory() {
        return this.foodInventory;
    }

    // You will also need this for your Staff view
    public List<Staff> getUsers() {
        return this.users;
    }


    // --- HELPER METHODS ---
    public String getZooName() { return zooName; }
    public String getAddress() { return address; }
    public String getLastMessage() { return lastMessage; }
    public List<Habitat> getHabitats() { return habitats; }
    public boolean isStaffLoggedIn() { return loggedInUser != null; }
    public Staff getLoggedInStaff() { return loggedInUser; }

    private void setDefaultManager() {
        Manager admin = new Manager("M001", "Admin", "admin1", "12345678", 1500);
        Keeper k = new Keeper("K001", "Keeper", "keeper1", "12345678", 1500);
        registerUser(admin);
        registerUser(k);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Fixed the illegal method name "throw new ZooException"
    private void setLastMessage(String msg) {
        lastMessage = msg;
    }

    private boolean requireStaffLogin() throws ZooException {
        if (loggedInUser == null) {
            throw new ZooException("Action denied: staff must login first.");
        }
        if (!loggedInUser.isActive()) {
            loggedInUser = null;
            throw new ZooException("Action denied: staff is inactive (auto logout).");
        }
        return true;
    }

    private boolean requirePermission(String action) throws ZooException {
        if (!requireStaffLogin()) return false;
        if (!loggedInUser.can(action)) {
            throw new ZooException("Permission denied for action: " + action);
        }
        return true;
    }

    public void registerUser(Staff newUser) {
        users.add(newUser);
    }

    // --- LOGIN SYSTEM ---
    public void login(String username, String password) throws ZooException {
        if (isBlank(username) || password == null) {
                throw new ZooException("Login failed: missing email/password.");
            }

        // Logic flow kept: search user, check active, check password
        // Staff user = users.stream()
        //     .filter(u -> u.getUsername().equalsIgnoreCase(username.trim()))
        //     .findFirst()
        //     .orElseThrow(() -> new ZooException("Login failed: username not found."));

        StaffDAO staffDAO = new StaffDAO(); 
        Staff user = staffDAO.login(username, password);

        if (!user.isActive()) {
            throw new ZooException("Login failed: staff is inactive.");
        }

        if (!user.checkPassword(password)) {
            throw new ZooException("Login failed: wrong password.");
        }

        loggedInUser = user;
        setLastMessage("Login success. Welcome " + user.getName() + "!");
    }

    public void logout() throws ZooException {
        if (loggedInUser != null) {
            String name = loggedInUser.getUsername();
            loggedInUser = null;
            setLastMessage(name + " logged out."); // Replaced throw with message for success
        }
    }

    // --- STAFF MANAGEMENT ---
    public void createStaff(String staffId, String fullName, String position,
                            String username, String password, float salary) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId) || isBlank(username)) {
            throw new ZooException("Cannot create staff: staffId/username is empty.");
        }

        boolean exists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username.trim()));
        if (exists) {
            throw new ZooException("Cannot create staff: username already exists.");
        }

        if (position.equalsIgnoreCase("Manager")) {
            users.add(new Manager(staffId, fullName, username, password, salary));
            setLastMessage("Manager created successfully.");
        } else if (position.equalsIgnoreCase("Keeper")) {
            users.add(new Keeper(staffId, fullName, username, password, salary));
            setLastMessage("Keeper created successfully.");
        }
    }

    public void removeStaff(String staffId) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(staffId)) {
            throw new ZooException("staffId is empty.");
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(staffId.trim())) {
                String name = users.get(i).getUsername();
                users.remove(i);
                setLastMessage("Staff " + name + " removed successfully.");
                return; 
            }
        }
        throw new ZooException("Staff not found.");
    }

    // --- ANIMAL & HABITAT MANAGEMENT ---
    public void addAnimalToHabitat(Animal animal, Habitat habitat) throws InvalidHabitatException, ZooException {
        if (!requirePermission(ANIMAL_MANAGE)) return;

        if (!habitat.canHouse(animal)) {
            throw new InvalidHabitatException("Cannot add " + animal.getName() + " to " + habitat.getName() + ": incompatible habitat.");
        }

        habitat.getAnimals().add(animal);
        System.out.println(animal.getName() + " added to " + habitat.getName());
    }

    public void removeAnimalFromHabitat(Animal animal, Habitat habitat) throws ZooException {
        if (!requirePermission(ANIMAL_MANAGE)) return;

        if (!getLoggedInStaff().canAccessHabitat(habitat)) {
            throw new ZooException("You are not assigned to this habitat.");
        }
        habitat.removeAnimal(animal);
        setLastMessage(animal.getName() + " removed from " + habitat.getName());
    }

    public void createHabitat(String type, Food food) throws ZooException {
        if (!requirePermission(HABITAT_MANAGE)) return;

        Habitat h = defineHabitats(type, food);
        if (h != null) {
            habitats.add(h);
            setLastMessage("Habitat created successfully.");
        }
    }

    public void assignHabitatToKeeper(String staffId, Habitat habitat) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        Staff staff = users.stream()
            .filter(s -> s.getId().equalsIgnoreCase(staffId.trim()))
            .findFirst()
            .orElseThrow(() -> new ZooException("Staff not found."));

        try {
            staff.assignHabitat(habitat);
            setLastMessage("Habitat assigned successfully.");
        } catch (UnsupportedOperationException e) {
            throw new ZooException(staff.getUsername() + " cannot be assigned habitats.");
        }
    }

    public void viewHabitatSchedules(Habitat habitat) throws ZooException {
        if (!requirePermission(VIEW_REPORT)) return;    


        if (habitat == null) {
        throw new ZooException("Cannot view schedules: Habitat is null.");
        }

        try {
                System.out.println("--- Schedules for " + habitat.getName() + " ---");
                habitat.getFeedingTimes().forEach(s -> System.out.println(s)); 
        } catch (Exception e) {
            throw new ZooException("Error viewing schedules: " + e.getMessage());
        }
    }

    // --- FOOD & SCHEDULE ---
    public void addFoodToInventory(Food food) throws ZooException {
        if (!requirePermission(FOOD_MANAGE)) return;
        foodInventory.add(food);
        setLastMessage("Food added to central inventory.");
    }

    public void feedHabitat(Habitat habitat, int foodId, double amount) throws ZooException {
        if (!requirePermission(FOOD_MANAGE)) return;

        if (!getLoggedInStaff().canAccessHabitat(habitat)) {
            throw new ZooException("You are not assigned to this habitat.");
        }

        Food food = foodInventory.stream()
                .filter(f -> f.getId() == foodId)
                .findFirst()
                .orElseThrow(() -> new ZooException("Food not found."));

        if (food.getStock() < amount) {
            throw new ZooException("Not enough food in storage.");
        }



        food.setStock( (food.getStock() - amount));
        setLastMessage("Habitat fed successfully.");
    }

    public void addScheduleToHabitat(Schedule schedule, Habitat habitat) throws ZooException {
        if (!requirePermission(SCHEDULE_MANAGE)) return;
        habitat.addSchedule(schedule);
        setLastMessage("Schedule added.");
    }

    // --- REPORTS ---
    public void viewZooReport() throws ZooException {
        if (requirePermission(VIEW_REPORT)) {
            ZooReport summaryReport = () -> {
                System.out.println("--- " + getZooName().toUpperCase() + " SUMMARY REPORT ---");
                System.out.println("Total Staff: " + users.size());
                System.out.println("Active Habitats: " + habitats.size());
                System.out.println("Generated by: " + getLoggedInStaff().getName());
            };
            summaryReport.generateReport();
        }
    }

    private Habitat defineHabitats(String type, Food food) throws ZooException {
        if (type.equals("forest")) return new Forest(null, food);
        if (type.equals("ocean")) return new Ocean(null, food);
        if (type.equals("savannah")) return new Savannah(null, food);
        
        throw new ZooException("Unknown habitat type. Use: forest, ocean, savannah.");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Zoo{");
        sb.append("zooName=").append(zooName);
        sb.append(", address=").append(address);
        sb.append(", lastMessage=").append(lastMessage);
        sb.append(", users=").append(users);
        sb.append(", habitats=").append(habitats);
        sb.append(", foodInventory=").append(foodInventory);
        sb.append(", loggedInUser=").append(loggedInUser);
        sb.append('}');
        return sb.toString();
    } 

 

}