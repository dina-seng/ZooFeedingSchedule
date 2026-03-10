package com.zoo.main;

import com.zoo.interfaces.IHabitat;
import com.zoo.interfaces.IStaff;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.staff_roles.Manager;
import com.zoo.services.Schedule;
import com.zoo.services.Zoo;
import java.util.Scanner;

public class ZooMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Zoo zoo = new Zoo("Safari Zoo", "Phnom Penh");
        printMainMenu();
        System.out.print("Choose: ");
        int choice = sc.nextInt();
        sc.nextLine();
        do {
            if (!zoo.isStaffLoggedIn()) {
                switch (choice) {
                    case 1: {
                        System.out.print("Username: ");
                        String username = sc.nextLine();

                        System.out.print("Password: ");
                        String password = sc.nextLine();

                        zoo.login(username.trim(), password.trim());
                        System.out.println(zoo.getLastMessage());
                        break;
                    }

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } else {

                if (zoo.getLoggedInStaff() instanceof Manager) {
                    int mainChoice;
                    do {
                        printManagerMenu(zoo);
                        System.out.print("Choose: ");
                        mainChoice = sc.nextInt();
                        sc.nextLine();

                        switch (mainChoice) {

                            case 1: { // Manage Animals
                                manageAnimals(zoo, sc);
                                break;
                            }

                            case 2: { // Manage Schedule
                                manageSchedule(zoo, sc);
                                break;
                            }

                            case 3: { // Manage Staff
                                manageStaff(zoo, sc);
                                break;
                            }

                            case 4: { // Manage Habitats
                                manageHabitats(zoo, sc);
                                break;
                            }

                            case 5: { // Manage Foods
                                System.out.println("--- Add New Food ---");
                                System.out.print("Food Name: ");
                                String foodName = sc.nextLine().trim();
                                System.out.print("Quantity: ");
                                int quantity = sc.nextInt();
                                sc.nextLine();
                                System.out.print("Expiry date: ");
                                String expiryDate = sc.nextLine().trim();
                                System.out.print("CostPerUnit: ");
                                float cost = sc.nextFloat();
                                sc.nextLine();

                                Food food = new Food(foodName, quantity, expiryDate, cost);
                                zoo.addFoodToInventory(food);
                                System.out.println(zoo.getLastMessage());
                                break;
                            }

                            case 6: { // View Habitat Schedule
                                if (zoo.getHabitats().isEmpty()) {
                                    System.out.println("No habitats available.");
                                    break;
                                }

                                System.out.print("Habitat name: ");
                                String habName = sc.nextLine().trim();
                                for (IHabitat h : zoo.getHabitats()) {
                                    if (h.getName().equalsIgnoreCase(habName)) {
                                        zoo.viewHabitatSchedules(h);
                                        break;
                                    }
                                }
                                break;
                            }

                            case 7: { // View Zoo Report
                                zoo.viewZooReport();
                                break;
                            }

                            case 8: { // Logout
                                zoo.logout();
                                System.out.println(zoo.getLastMessage());
                                break;
                            }

                            case 0:
                                System.out.println("Goodbye!");
                                choice = 0;
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    } while (mainChoice != 0 && mainChoice != 8);
                    
                } else {
                    int mainChoice;
                    do {
                        printStaffMenu(zoo);
                        System.out.print("Choose: ");
                        mainChoice = sc.nextInt();
                        sc.nextLine();

                        switch (mainChoice) {

                            case 1: { // Manage Animals
                                manageAnimals(zoo, sc);
                                break;
                            }

                            case 2: { // View Habitat Schedule
                                if (zoo.getHabitats().isEmpty()) {
                                    System.out.println("No habitats available.");
                                    break;
                                }

                                System.out.print("Habitat name: ");
                                String habName = sc.nextLine();
                                for (IHabitat h : zoo.getHabitats()) {
                                    if (h.getName().equalsIgnoreCase(habName)) {
                                        zoo.viewHabitatSchedules(h);
                                        break;
                                    }
                                }
                                break;
                            }

                            case 3: { // View Zoo Report
                                zoo.viewZooReport();
                                break;
                            }

                            case 4: { // Logout
                                zoo.logout();
                                System.out.println(zoo.getLastMessage());
                                break;
                            }

                            case 0:
                                System.out.println("Goodbye!");
                                choice = 0;
                                break;

                            default:
                                System.out.println("Invalid choice.");
                        }
                    } while (mainChoice != 0 && mainChoice != 4);
                }
            }

        } while (choice != 0);

        sc.close();
    }

    private static void printMainMenu() {
        System.out.println("\n=== ZOO SYSTEM ===");
        System.out.println("1) Staff Login");
        System.out.println("0) Exit");
    }

    private static void printStaffMenu(Zoo zoo) {
        System.out.println("\n=== STAFF MENU ===");
        System.out.println("Logged in: " + zoo.getLoggedInStaff().getUsername());
        System.out.println("1) Manage Animals");
        System.out.println("2) View Habitat Schedule");
        System.out.println("3) View Zoo Report");
        System.out.println("4) Logout");
        System.out.println("0) Exit");
    }

    private static void printManagerMenu(Zoo zoo) {
        System.out.println("\n=== MANAGER MENU ===");
        System.out.println("Logged in: " + zoo.getLoggedInStaff().getUsername());
        System.out.println("1) Manage Animals");
        System.out.println("2) Manage Schedule");
        System.out.println("3) Manage Staff");
        System.out.println("4) Manage Habitats");
        System.out.println("5) Manage Foods");
        System.out.println("6) View Habitat Schedule");
        System.out.println("7) View Zoo Report");
        System.out.println("8) Logout");
        System.out.println("0) Exit");
    }

    // ===== SUBMENU METHODS =====
    private static void manageAnimals(Zoo zoo, Scanner sc) {
        System.out.println("\n--- Manage Animals ---");
        System.out.println("1) Add Animal");
        System.out.println("2) Remove Animal");
        System.out.println("0) Back");
        System.out.print("Choose: ");
        int subChoice = sc.nextInt();
        sc.nextLine();

        switch (subChoice) {
            case 1: { // Add Animal
                System.out.print("Animal Name: ");
                String name = sc.nextLine();

                System.out.print("Age: ");
                int age = sc.nextInt();
                sc.nextLine();

                System.out.print("Species: ");
                String species = sc.nextLine();

                System.out.print("Weight: ");
                double weight = sc.nextDouble();
                sc.nextLine();

                Animal animal = new Animal(name, age, species, weight);

                if (zoo.getHabitats().isEmpty()) {
                    System.out.println("No habitats available.");
                    break;
                }

                IHabitat habitat = zoo.getHabitats().get(0);
                zoo.addAnimalToHabitat(animal, habitat);
                System.out.println(zoo.getLastMessage());
                break;
            }

            case 2: { // Remove Animal
                System.out.print("Animal ID: ");
                String animalId = sc.nextLine();

                System.out.print("Habitat name: ");
                String habitatName = sc.nextLine();

                IHabitat selectedHabitat = null;
                Animal findAnimal = null;

                for (IHabitat h : zoo.getHabitats()) {
                    if (h.getName().equalsIgnoreCase(habitatName)) {
                        selectedHabitat = h;
                        for (Animal a : h.getAnimals()) {
                            if (a.getId() == Integer.parseInt(animalId)) {
                                findAnimal = a;
                                break;
                            }
                        }
                        break;
                    }
                }

                if (selectedHabitat == null) {
                    System.out.println("Habitat not found.");
                    break;
                }

                if (findAnimal == null) {
                    System.out.println("Animal not found in this habitat.");
                    break;
                }

                zoo.removeAnimalFromHabitat(findAnimal, selectedHabitat);
                System.out.println(zoo.getLastMessage());
                break;
            }
        }
    }

    private static void manageSchedule(Zoo zoo, Scanner sc) {
        System.out.println("\n--- Manage Schedule ---");
        System.out.println("1) Add Schedule");
        System.out.println("2) Remove Schedule");
        System.out.println("0) Back");
        System.out.print("Choose: ");
        int subChoice = sc.nextInt();
        sc.nextLine();

        switch (subChoice) {
            case 1: { // Add Schedule
                if (zoo.getHabitats().isEmpty()) {
                    System.out.println("No habitats available.");
                    break;
                }
                
                IStaff keeper = zoo.getLoggedInStaff();
                if (!(keeper instanceof com.zoo.models.staff_roles.Keeper)) {
                    System.out.println("Only keepers can be assigned to schedules.");
                    break;
                }

                System.out.print("Schedule Date (YYYY-MM-DD): ");
                String date = sc.nextLine().trim();

                System.out.print("Schedule Time (HH:MM AM/PM): ");
                String time = sc.nextLine().trim();

                Schedule newSchedule = new Schedule(keeper, date, time);
                
                System.out.print("Habitat name: ");
                String habitatName = sc.nextLine().trim();
                
                IHabitat habitat = null;
                for (IHabitat h : zoo.getHabitats()) {
                    if (h.getName().equalsIgnoreCase(habitatName)) {
                        habitat = h;
                        break;
                    }
                }
                
                if (habitat == null) {
                    System.out.println("Habitat not found.");
                    break;
                }
                
                zoo.addScheduleToHabitat(newSchedule, habitat);
                System.out.println(zoo.getLastMessage());
                break;
            }

            case 2: { // Remove Schedule
                if (zoo.getHabitats().isEmpty()) {
                    System.out.println("No habitats available.");
                    break;
                }

                System.out.print("Schedule ID: ");
                int scheduleId = sc.nextInt();
                sc.nextLine();
                
                for (IHabitat h : zoo.getHabitats()) {
                    for (Schedule s : h.getFeedingTimes()) {
                        if (s.getId() == scheduleId) {
                            zoo.removeScheduleFromHabitat(s, h);
                            System.out.println(zoo.getLastMessage());
                            return;
                        }
                    }
                }
                System.out.println("Schedule not found.");
                break;
            }
        }
    }

    private static void manageStaff(Zoo zoo, Scanner sc) {
        System.out.println("\n--- Manage Staff ---");
        System.out.println("1) Add Staff");
        System.out.println("2) Remove Staff");
        System.out.println("0) Back");
        System.out.print("Choose: ");
        int subChoice = sc.nextInt();
        sc.nextLine();

        switch (subChoice) {
            case 1: { // Add Staff
                System.out.println("--- Add New Staff ---");

                System.out.print("Enter Staff ID: ");
                String staffId = sc.nextLine().trim();

                System.out.print("Enter Full Name: ");
                String fullName = sc.nextLine().trim();

                System.out.print("Enter Position (Keeper, Manager): ");
                String position = sc.nextLine().trim();

                System.out.print("Enter Username: ");
                String username = sc.nextLine().trim();

                System.out.print("Enter Password: ");
                String password = sc.nextLine().trim();      

                System.out.print("Enter Salary: ");
                float salary = Float.parseFloat(sc.nextLine().trim());     

                zoo.createStaff(staffId, fullName, position, username, password, salary);
                System.out.println(zoo.getLastMessage());
                break;
            }

            case 2: { // Remove Staff
                System.out.print("Staff ID to remove: ");
                String staffId = sc.nextLine().trim();
                zoo.removeStaff(staffId);
                System.out.println(zoo.getLastMessage());
                break;
            }
        }
    }

    private static void manageHabitats(Zoo zoo, Scanner sc) {
        System.out.println("\n--- Manage Habitats ---");
        System.out.println("1) Create Habitat");
        System.out.println("2) View animals in habitat");
        System.out.println("3) Assign Habitat to Keeper");
        System.out.println("0) Back");
        System.out.print("Choose: ");
        int subChoice = sc.nextInt();
        sc.nextLine();

        switch (subChoice) {
            case 1: { // Create Habitat
                System.out.println("--- Create New Habitat ---");
                
                System.out.print("Habitat Type (forest/ocean/savannah): ");
                String habType = sc.nextLine().trim();

                System.out.print("Food Name for habitat: ");
                String foodName = sc.nextLine().trim();
                System.out.print("Food Quantity: ");
                int foodQty = sc.nextInt();
                sc.nextLine();
                System.out.print("Food Expiry Date: ");
                String foodExpiry = sc.nextLine().trim();
                System.out.print("Food Cost Per Unit: ");
                float foodCost = sc.nextFloat();
                sc.nextLine();
                
                Food habFood = new Food(foodName, foodQty, foodExpiry, foodCost);
                zoo.addFoodToInventory(habFood);
                zoo.createHabitat(habType, habFood);
                System.out.println(zoo.getLastMessage());
                break;
            }

            case 2: {
                System.out.print("Habitat name (eg: Ocean1): ");
                String habitatName = sc.nextLine().trim();
                for (IHabitat h : zoo.getHabitats()) {
                    if (h.getName().equals(habitatName)) {
                        h.showAnimals();
                        break;
                    }
                }
                break;
            }

            case 3: { // Assign Habitat to Keeper
                System.out.println("--- Assign Habitat to Keeper ---");
                System.out.print("Keeper ID: ");
                String keeperId = sc.nextLine().trim();
                
                System.out.print("Habitat Name: ");
                String habitatName = sc.nextLine().trim();
                
                IHabitat selectedHabitat = null;
                for (IHabitat h : zoo.getHabitats()) {
                    if (h.getName().equalsIgnoreCase(habitatName)) {
                        selectedHabitat = h;
                        break;
                    }
                }
                
                if (selectedHabitat == null) {
                    System.out.println("Habitat not found.");
                    break;
                }
                
                zoo.assignHabitatToKeeper(keeperId, selectedHabitat);
                System.out.println(zoo.getLastMessage());
                break;
            }
        }
    }
}