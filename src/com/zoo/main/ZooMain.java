package com.zoo.main;

import com.zoo.exceptions.ZooException;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.habitat_types.Habitat;
import com.zoo.models.staff_roles.Manager;
import com.zoo.services.Zoo;
import java.util.Scanner;

public class ZooMain {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        Zoo zoo = new Zoo("Safari Zoo", "Phnom Penh");
        boolean running = true;

        System.out.println("Welcome to " + zoo.getZooName() + " Management System");

        while (running) {
            try {
                if (!zoo.isStaffLoggedIn()) {
                    printMainMenu();
                    System.out.print("Choose: ");
                    int choice = Integer.parseInt(sc.nextLine());

                    switch (choice) {
                        case 1 -> handleLogin(zoo);
                        case 2 -> viewAsGuest(zoo);
                        case 0 -> {
                            System.out.println("Goodbye!");
                            running = false;
                        }
                        default -> System.out.println("Invalid choice.");
                    }
                } else {
                    if (zoo.getLoggedInStaff() instanceof Manager) {
                        handleManagerFlow(zoo);
                    } else {
                        handleStaffFlow(zoo);
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
        }
        sc.close();
    }

    // --- Authentication Flow ---
    private static void handleLogin(Zoo zoo) {
       try {
        System.out.println("\n--- Staff Login ---");
        System.out.print("Enter Email: "); //dina@zoo.com or keeper@zoo.com 
        String email = sc.nextLine().trim();
        
        System.out.print("Enter Password: "); // pw both is 12345
        String password = sc.nextLine().trim();

        // This now checks MySQL instead of RAM!
        zoo.login(email, password);
        
        System.out.println("Welcome back, " + zoo.getLoggedInStaff().getName() + "!");
        
    } catch (ZooException e) {
        // This catches the error from the DAO or the Login method
        System.err.println("\n[LOGIN ERROR] " + e.getMessage());
    }
    }

    // --- Manager Logic ---
    private static void handleManagerFlow(Zoo zoo) {
        printManagerMenu(zoo);
        System.out.print("Choose: ");
        try {
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> manageAnimals(zoo);
                case 2 -> manageSchedule(zoo);
                case 3 -> manageStaff(zoo);
                case 4 -> manageHabitats(zoo);
                case 5 -> addNewFood(zoo);
                case 6 -> viewSchedules(zoo);
                case 7 -> zoo.viewZooReport();
                case 8 -> viewAllStaffFromDB(zoo); 
                case 9 -> viewFoodInventoryFromDB(zoo);
                case 10 -> viewAllStaffFromDB(zoo);
                case 11 -> {
                    zoo.logout();
                    System.out.println("Logged out successfully.");
                }
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.err.println("Operation Failed: " + e.getMessage());
        }
    }

    // --- Staff Logic ---
    private static void handleStaffFlow(Zoo zoo) {
        printStaffMenu(zoo);
        System.out.print("Choose: ");
        try {
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> manageAnimals(zoo);
                case 2 -> viewSchedules(zoo);
                case 3 -> zoo.viewZooReport();
                case 4 -> zoo.logout();
                case 0 -> System.exit(0);
                default -> System.out.println("Invalid option.");
            }
        } catch (Exception e) {
            System.err.println("Operation Failed: " + e.getMessage());
        }
    }


    // --- Guest Logic ---
    private static void viewAsGuest(Zoo zoo) {
        boolean back = false;
        while (!back) {
            System.out.println("\n---  Guest Information Center ---");
            System.out.println("1) View Our Animals");
            System.out.println("2) View Habitats");
            System.out.println("3) View Food Inventory");
            System.out.println("4) View Feeding Performance");
            System.out.println("0) Back to Main Menu");
            System.out.print("Choose: ");
            
            try {
                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    
                    case 1 -> viewAnimalsGuest(zoo);
                    case 2 -> viewHabitatsGuest(zoo);
                    case 3 -> viewFoodInventoryFromDB(zoo); // Reuse your existing method!
                    case 4 -> viewFeedingPerformance(zoo);
                    case 0 -> back = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: Please enter a number.");
            }
        }
    }

    // --- Sub-Modules ---

    private static void manageAnimals(Zoo zoo) throws ZooException {
        System.out.println("\n1) Add Animal\n2) Remove Animal\n0) Back");
        int sub = Integer.parseInt(sc.nextLine());
        
        if (sub == 1) {
            System.out.print("Name: "); String name = sc.nextLine();
            System.out.print("Species: "); String species = sc.nextLine();
            System.out.print("Age: "); int age = Integer.parseInt(sc.nextLine());
            System.out.print("Weight: "); double weight = Double.parseDouble(sc.nextLine());

            if (zoo.getHabitats().isEmpty()) throw new ZooException("No habitats exist yet!");

            // Show habitats for selection
            for (int i = 0; i < zoo.getHabitats().size(); i++) {
                System.out.println(i + ") " + zoo.getHabitats().get(i).getName());
            }
            System.out.print("Select Habitat Index: ");
            int idx = Integer.parseInt(sc.nextLine());
            
            zoo.addAnimalToHabitat(new Animal(name, age, species, weight), zoo.getHabitats().get(idx));
            System.out.println("Animal added successfully.");
        }
    }

    private static void addNewFood(Zoo zoo) throws ZooException {
        System.out.print("Food Name: "); String name = sc.nextLine();
        System.out.print("Stock: "); double stock = Double.parseDouble(sc.nextLine());
        System.out.print("Expiry (YYYY-MM-DD): "); String expiry = sc.nextLine();
        System.out.print("Cost: "); double cost = Double.parseDouble(sc.nextLine());

        zoo.addFoodToInventory(new Food(name, stock, expiry, cost));
        System.out.println("Food inventory updated.");
    }

    private static void manageHabitats(Zoo zoo) throws ZooException {
        System.out.println("1) Create Habitat\n2) Show Animals in Habitat");
        int sub = Integer.parseInt(sc.nextLine());
        
        if (sub == 1) {
            System.out.print("Type (forest/ocean/savannah): ");
            String type = sc.nextLine();
            // In a real system, you'd select food from inventory here
            zoo.createHabitat(type, null); 
            System.out.println("Habitat " + type + " created.");
        } else if (sub == 2) {
            System.out.print("Habitat Name: ");
            String name = sc.nextLine();
            for(Habitat h : zoo.getHabitats()) {
                if(h.getName().equalsIgnoreCase(name)) h.showAnimals();
            }
        }
    }

    private static void viewSchedules(Zoo zoo) {


        if (zoo.getHabitats().isEmpty()) {
        System.out.println("No habitats registered in the system.");
        return;
        }

        for (Habitat h : zoo.getHabitats()) {
                try {
                    // Call your service method
                    zoo.viewHabitatSchedules(h);
                } catch (ZooException e) {
                    // Log the error for this specific habitat and keep going
                    System.err.println("Could not load schedules for " + h.getName() + ": " + e.getMessage());
                }
            }
    }


    private static void viewAllStaffFromDB(Zoo zoo) {
        System.out.println("\n--- Real-Time Staff Data (MySQL) ---");
        if (zoo.getUsers().isEmpty()) {
            System.out.println("No staff found. Check your StaffDAO!");
        } else {
            for (Object staffObj : zoo.getUsers()) {
                // Casting to your IStaff interface or Staff class
                com.zoo.interfaces.IStaff s = (com.zoo.interfaces.IStaff) staffObj;
                System.out.println("ID: " + s.getId() + " | Name: " + s.getName() + " | Role: " + s.getClass().getSimpleName());
            }
        }
    }

    private static void viewFoodInventoryFromDB(Zoo zoo) {
        System.out.println("\n--- Real-Time Food Stock (MySQL) ---");
        if (zoo.getFoodInventory().isEmpty()) {
            System.out.println("Inventory empty. Did you run the SQL Insert?");
        } else {
            System.out.printf("%-5s | %-15s | %-10s | %-12s\n", "ID", "Name", "Stock", "Expiry");
            System.out.println("---------------------------------------------------------");
            for (Food f : zoo.getFoodInventory()) {
                System.out.printf("%-5d | %-15s | %-10.2f | %-12s\n", 
                    f.getId(), f.getName(), f.getStock(), f.getExpiryDate());
            }
        }
    }

    private static void viewAnimalsGuest(Zoo zoo) {
        System.out.println("\n---  Our Amazing Animals ---");
        if (zoo.getHabitats().isEmpty()) {
            System.out.println("The animals are currently resting. (No data in DB)");
        } else {
            System.out.printf("%-15s | %-15s | %-5s | %-10s\n", "Name", "Species", "Age", "Habitat");
            System.out.println("------------------------------------------------------------");
            for (Habitat h : zoo.getHabitats()) {
                for (Animal a : h.getAnimals()) {
                    System.out.printf("%-15s | %-15s | %-5d | %-10s\n", 
                        a.getName(), a.getSpecies(), a.getAge(), h.getName());
                }
            }
        }
    }

    private static void viewHabitatsGuest(Zoo zoo) {
        System.out.println("\n---  Our Habitats ---");
        if (zoo.getHabitats().isEmpty()) {
            System.out.println("No habitats found in the database.");
        } else {
            for (Habitat h : zoo.getHabitats()) {
                System.out.println( h.getName() + " [" + h.getClass().getSimpleName() + "]");
                System.out.println("   Capacity: " + h.getAnimals().size() + " animals currently inside.");
            }
        }
    }

    private static void viewFeedingPerformance(Zoo zoo) {
        System.out.println("\n---  Feeding Performance ---");
        if (zoo.getHabitats().isEmpty()) {
            System.out.println("No habitats to show performance for.");
        } else {
            for (Habitat h : zoo.getHabitats()) {
                System.out.println("📍 " + h.getName() + ": " + h.getFeedingPerformance() + "%");
            }
        }
    }

    // --- Menu Printers ---
    private static void printMainMenu() {
        System.out.println("\n--- Safari Zoo ---");
        System.out.println("1) Staff Login");
        System.out.println("2) View Zoo as Guest (Live DB Data)");
        System.out.println("0) Exit");
    }

    private static void printStaffMenu(Zoo zoo) {
        System.out.println("\n--- Staff: " + zoo.getLoggedInStaff().getName() + " ---");
        System.out.println("1) Manage Animals\n2) View Schedules\n3) Report\n4) Logout\n0) Exit");
    }

 private static void printManagerMenu(Zoo zoo) {
        System.out.println("\n--- Manager: " + zoo.getLoggedInStaff().getName() + " ---");
        System.out.println("1) Animals      2) Schedules    3) Staff");
        System.out.println("4) Habitats     5) Add Food     6) View Schedule");
        System.out.println("7) Report       8) Logout");
        System.out.println("9) [DB] VIEW FOOD STOCK   10)[DB] VIEW ALL STAFF "); // New labels
        System.out.println("0) Exit");
    }
    
    // Stub methods for other logic
    private static void manageSchedule(Zoo zoo) {}
    private static void manageStaff(Zoo zoo) {}
}