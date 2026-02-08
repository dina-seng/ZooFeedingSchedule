
// Main class to demonstrate the functionality of the zoo management system 

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   WELCOME TO THE ZOO MANAGEMENT SYSTEM   ");
        System.out.println("==========================================\n");

        // 1. PROJECT STATUS: Accessing static data from ZooResource
        System.out.println("--- Current Zoo Inventory ---");
        System.out.println("Total Animals: " + ZooResource.animalCount);
        System.out.println("Total Staff: " + ZooResource.staffCount);
        System.out.println("Total Habitats: " + ZooResource.habitatCount);
        System.out.println("-----------------------------\n");

        // 2. RELATIONSHIP PROOF: Show the current feeding schedule
        if (ZooResource.currentSchedule != null) {
            Schedule today = ZooResource.currentSchedule;
            
            System.out.println("--- Active Feeding Task ---");
            System.out.println("Time: " + today.getTime());
            
            // Reaching through the Schedule object to get Staff and Habitat data
            // This proves your Access Control and Association are working!
            System.out.println("Keeper on Duty: " + today.getAssignedStaff().getName());
            System.out.println("Destination: " + today.getTargetHabitat().getHabitatID() + 
                               " (" + today.getTargetHabitat().getSpecies() + " Zone)");
            
            System.out.println("Status: " + (today.isCompleted() ? " Completed" : " Pending"));
            System.out.println("-----------------------------\n");
        }

        // 3. ENCAPSULATION PROOF: Try to update stock safely
        System.out.println("--- Food Stock Management ---");
        Food pandaFood = ZooResource.foods[0]; // Bamboo
        System.out.println("Current " + pandaFood.getName() + " Stock: " + pandaFood.getStockStatus());
        
        System.out.println("Action: Animals are eating...");
        // Using a setter (encapsulation) to update stock
        double newStock = pandaFood.getStockStatus() - 15.5;
        pandaFood.setStock(newStock); 
        
        System.out.println("Updated " + pandaFood.getName() + " Stock: " + pandaFood.getStockStatus());
        System.out.println("-----------------------------\n");

        // 4. HABITAT REPORT: Listing animals in a specific zone
        Habitat jungle = ZooResource.habitats[1];
        System.out.println("--- Habitat Report: " + jungle.getHabitatID() + " ---");
        System.out.println("Species: " + jungle.getSpecies());
        // Since we protected our arrays, we use the toString() or a getter
        System.out.println(jungle.toString());
        System.out.println("==========================================");
    }
}