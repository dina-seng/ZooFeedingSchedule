public class Main {
    public static void main(String[] agrs) {
        // Copy a primitive, modify the copy, original remains unchanged
        System.out.println("\nF1 - Primitive copy proof");
        int animalID = ZooResource.animals[1].animalID;
        int copyAnimalID = animalID;
        copyAnimalID = 23;
        System.out.println("The copied animalID: " + copyAnimalID);
        System.out.println("The original animalID: " + animalID);
        // Two variables reference the same object; change is visible everywhere
        System.out.println("\nF2 - Reference copy proof");
        Animal testAnimal = ZooResource.animals[2];
        Animal clonedAnimal = testAnimal;
        System.out.println("The animal's id before change: " + testAnimal.animalID);
        clonedAnimal.animalID = 3;

        System.out.println("After change:");
        System.out.println("The original animal's id: " + testAnimal.animalID);
        System.out.println("The clone animal's id: " + clonedAnimal.animalID);

        // Objects inside arrays reflect later modifications
        System.out.println("\nF3 - Array stores references proof");
        Animal animals = ZooResource.animals[1];
        if (animals != null) {
            System.out.println("Animal's id before change: " + animals.animalID);
            animals.animalID = 33;  // Object change
            System.out.println("Animal's id after change: " + animals.animalID);
        }

        // Stored snapshot values do not change after the original object changes
        System.out.println("\nF4 - Snapshot proof");
        Animal animal = ZooResource.animals[1];
        int snapShotId = animal.animalID;
        System.out.println("The original animal's id: " + animal.animalID);
        animal.animalID = 333;  // Animal object change
        System.out.println("After animal's id change ( snapshot ): " + snapShotId);
        System.out.println("Original animal's id: " + animal.animalID);

        System.out.println("\nF5 - Staff array reference proof");
        Staff staff1 = new Staff("S001", "Alice", "Zookeeper");
        ZooResource.addStaff(staff1);
        Staff staff2 = ZooResource.staffs[0];
        System.out.println("Staff name before change: " + staff2.name);
        staff2.name = "Bob";
        System.out.println("Staff name after change: " + staff2.name);
        System.out.println("Original staff name in array: " + ZooResource.staffs[0].name);
    }
}