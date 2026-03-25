package com.zoo.services;


import static com.zoo.controller.MySqlDatabaseConnection.getConnection;
import com.zoo.dao.*;
import com.zoo.exceptions.InvalidHabitatException;
import com.zoo.exceptions.InvalidNameException;
import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import com.zoo.models.habitat_types.Forest;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.habitat_types.Ocean;
import com.zoo.models.habitat_types.Savannah;
import com.zoo.models.staff_roles.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private List<Schedule> schedules = new ArrayList<>();
    private List<Animal> animals = new ArrayList<>(); 
    private AnimalDAO animalDAO = new AnimalDAO(); 
    private HabitatDAO habitatDAO = new HabitatDAO();
    private FoodDAO foodDAO = new FoodDAO();
    private StaffDAO staffDAO = new StaffDAO();
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    private Staff loggedInUser;

    public Zoo(String zooName, String address) {
        this.zooName = zooName.trim();
        this.address = address.trim();

        initializeData();

        System.out.println("System: Loaded " + animals.size() + " animals from database.");
    
        loggedInUser = null;
        lastMessage = "Zoo Created. Default: admin1 / 12345678";
    } 

    public void initializeData() {
        try {
            this.users = staffDAO.getAllStaff();
            this.foodInventory = foodDAO.getInventory();
            this.habitats = habitatDAO.getAllHabitats();          
            this.animals = animalDAO.getAllAnimals();
            this.schedules = scheduleDAO.getAllSchedules();

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
    public List<Food> getFoodInventory() {return this.foodInventory;}
    public List<Schedule> getSchedules() {return this.schedules;}

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
    public void createStaff(String fullName, String position,
                            String username, String password, float salary) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        if (isBlank(username)) {
            throw new ZooException("Cannot create staff: staffId/username is empty.");
        }

        boolean exists = users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username.trim()));
        if (exists) {
            throw new ZooException("Cannot create staff: username already exists.");
        }

        Staff newStaff = staffDAO.createStaff(fullName, position, username, password, salary);
        users.add(newStaff);
        setLastMessage(position + " created successfully.");
    }

    public void removeStaff(int staffId) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == staffId) {
                String name = users.get(i).getUsername();
                staffDAO.deleteStaff(staffId);
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

    public void assignHabitatToKeeper(int staffId, Habitat habitat) throws ZooException {
        if (!requirePermission(STAFF_MANAGE)) return;

        Staff staff = users.stream()
            .filter(s -> s.getId() == staffId).findFirst()
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
    public void addFoodToInventory(Food food) throws Exception {
        String sql = "INSERT INTO food (name, stock, expiry_date, costPerUnit) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, food.getName());
            ps.setDouble(2, food.getStock());
            ps.setString(3, food.getExpiryDate());
            ps.setDouble(4, food.getCostPerUnit());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) food.setId(rs.getInt(1));
            }

            foodInventory.add(food);
        }
    }

    public void removeFoodFromInventory(int id) throws Exception {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM food WHERE food id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        foodInventory.removeIf(f -> f.getId() == id);
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


    public static void validateName(String fieldName, String value) throws InvalidNameException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidNameException(fieldName, "(empty)");
        }
        try {
            Double.parseDouble(value.trim()); 
            throw new InvalidNameException(fieldName, value);
        } catch (NumberFormatException e) {
            // Valid name, do nothing
        }
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