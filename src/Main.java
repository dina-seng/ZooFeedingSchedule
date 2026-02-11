public class Main {
    public static void main(String[] args) {
        Zoo PhnomPenh = new Zoo("Safari",100,15,2000,30);

        Staff alice = new Staff("st1", "Alice", "Keeper-Forest", 350, 1);
        Staff dara = new Staff("st3", "Dara", "Keeper-Jungle", 400, 3);
        Staff bob   = new Staff("st5", "Bob", "Manager", 800, 1);

        Schedule s1 = new Schedule(alice,"10-02-2026","12:00AM");
        Schedule s2  = new Schedule(dara,"09-02-2026","8:00AM");

        Food bamboo  = new Food( "Bamboo", "Plant", 50.0, "2026-07-15", 2.5);
        Food carrot   = new Food("Carrot", "Plant", 60.0, "2026-06-20", 1.8);

        Habitat FR001 = new Habitat("FR001", "Panda", bamboo.getStock(), s1,bamboo);
        Habitat JG001 = new Habitat("JG001", "Monkey", carrot.getStock(), s2,carrot);

        bob.setAssignedHabitat(FR001,bob);
        bob.setAssignedHabitat(JG001,bob);

        Animal a = new Animal("riki",12,"new",23);
        PhnomPenh.addAnimal(a, bob);
        PhnomPenh.addAnimal(a, dara);

        System.out.println(PhnomPenh.toString());

         Animal animal = new Animal("lulu", 3, "Panda", 26);
         // Copy a primitive, modify the copy, original remains unchanged
         System.out.println("\nF1 - Primitive copy proof");
         int Id = animal.getAnimalID();
         int copyId = Id;
         copyId = 23;
         System.out.println("The copied id: " + copyId);
         System.out.println("The original id: " + Id);

         // Two variables reference the same object; change is visible everywhere
         System.out.println("\nF2 - Reference copy proof");
         double bam = bamboo.getCostPerUnit();
         System.out.println("The bamboo price before change: " + bam);
         bam = carrot.getCostPerUnit();
         System.out.println("After change:");
         System.out.println("The original bamboo: " + bamboo.getCostPerUnit());
         System.out.println("The bamboo price after copy price of carrot: " + bam);

         // Objects inside arrays reflect later modifications
         System.out.println("\nF3 - Array stores references proof");
         Animal test = new Animal("Jam",12,"kola",15);


         // Stored snapshot values do not change after the original object changes
         System.out.println("\nF4 - Snapshot proof");


    }
}