package com.zoo.main;

import com.zoo.services.*;
import com.zoo.models.*;
import com.zoo.models.staff_roles.*;
import com.zoo.staff_interface.IStaff;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize System
        Zoo phnomPenhZoo = new Zoo("Safari World");

        // 2. Create Different Roles (Requirement #2)
        IStaff manager = new Manager("M001", "Alice", "admin", "1234");
        IStaff keeper = new Keeper("K001", "Dara", "keeper1", "pass123");
        IStaff vet = new Vet("V001", "Dr. Bob", "vet1", "med123");

        // 3. Register Users in System (Requirement #3)
        phnomPenhZoo.registerUser(manager);
        phnomPenhZoo.registerUser(keeper);
        phnomPenhZoo.registerUser(vet);

        // 4. Create dummy data
        Animal panda = new Animal("Riki", 5, "Panda", 101);

        System.out.println("--- Starting Zoo System Demo ---");

        // --- SCENARIO 1: KEEPER LOGIN (Requirement #4 & #6) ---
        System.out.println("\n[Test 1: Keeper Login]");
        phnomPenhZoo.login("keeper1", "pass123");
        
        // Attempting a Manager-only action (Requirement #5)
        phnomPenhZoo.addAnimal(panda); // Should FAIL
        
        // Attempting a Keeper-allowed action
        phnomPenhZoo.feedAnimals(); // Should SUCCEED

        phnomPenhZoo.logout();

        // --- SCENARIO 2: MANAGER LOGIN (Requirement #4 & #6) ---
        System.out.println("\n[Test 2: Manager Login]");
        phnomPenhZoo.login("admin", "1234");
        
        // Attempting same action as Manager
        phnomPenhZoo.addAnimal(panda); // Should SUCCEED
        phnomPenhZoo.viewZooReport();

        // --- SCENARIO 3: VET LOGIN (Requirement #6) ---
        System.out.println("\n[Test 3: Vet Login]");
        phnomPenhZoo.login("vet1", "med123");
        phnomPenhZoo.performMedicalCheck(); // Should SUCCEED
        phnomPenhZoo.removeAnimal(panda);   // Should FAIL (Vet can't delete)
    }
}