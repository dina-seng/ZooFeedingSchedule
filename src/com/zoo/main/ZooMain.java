package com.zoo.main;

import com.zoo.interfaces.IHabitat;
import com.zoo.models.Animal;
import com.zoo.models.staff_roles.Manager;
import com.zoo.services.Zoo;
import java.util.Scanner;

public class ZooMain {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Zoo zoo = new Zoo("CADT Zoo", "Phnom Penh");
        int choice;
        do {

            if (!zoo.isStaffLoggedIn()) {
                printMainMenu();
                System.out.print("Choose: ");
                choice = sc.nextInt();
                sc.nextLine();

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
                    printManagerMenu(zoo);
                } else {
                    printStaffMenu(zoo);
                }

                System.out.print("Choose: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1: { // ADD_ANIMAL
                        System.out.print("Animal Name: ");
                        String name = sc.nextLine();

                        Animal animal = new Animal(name, 12, "Tiger", 120);

                        if (zoo.getHabitats().isEmpty()) {
                            System.out.println("No habitats available.");
                            break;
                        }

                        IHabitat habitat = zoo.getHabitats().get(0);
                        zoo.addAnimalToHabitat(animal, habitat);
                        System.out.println(zoo.getLastMessage());
                        break;
                    }

                    case 2: { // REMOVE_ANIMAL

                        System.out.print("Animal ID: ");
                        String animalId = sc.nextLine();

                        System.out.print("Habitat name: ");
                        String habitatName = sc.nextLine();

                        IHabitat selectedHabitat = null;
                        Animal findAnimal = null;

                        // Find habitat
                        for (IHabitat h : zoo.getHabitats()) {
                            if (h.getName().equalsIgnoreCase(habitatName)) {
                                selectedHabitat = h;
                                for (Animal a : h.getAnimals()) {
                                    if (a.getAnimalID() == Integer.parseInt(animalId)) {
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

                    case 3: { // VIEW_HABITAT
                        if (zoo.getHabitats().isEmpty()) {
                            System.out.println("No habitats available.");
                            break;
                        }

                        IHabitat habitat = zoo.getHabitats().get(0);
                        zoo.viewHabitatSchedules(habitat);
                        break;
                    }

                    case 4: { // VIEW_REPORT
                        zoo.viewZooReport();
                        break;
                    }

                    case 5: { // Logout
                        zoo.logout();
                        System.out.println(zoo.getLastMessage());
                        break;
                    }

                    case 0:
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
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
        System.out.println("1) Add Animal");
        System.out.println("2) Remove Animal");
        System.out.println("3) View Habitat Schedule");
        System.out.println("4) View Zoo Report");
        System.out.println("5) Logout");
        System.out.println("0) Exit");
    }
    private static void printManagerMenu(Zoo zoo) {
        System.out.println("\n=== MANAGER MENU ===");
        System.out.println("Logged in: " + zoo.getLoggedInStaff().getUsername());
        System.out.println("1) Add Animal");
        System.out.println("2) Remove Animal");
        System.out.println("3) Add Schedule");
        System.out.println("4) Remove Schedule");
        System.out.println("5) View Habitat");
        System.out.println("6) View Zoo Report");
        System.out.println("7) Manage Foods");
        System.out.println("8) Logout");
        System.out.println("0) Exit");
    }
}