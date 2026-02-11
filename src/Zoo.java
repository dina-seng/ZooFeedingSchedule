import java.util.Arrays;

public class Zoo {
    private String zooName;
    private int animalCount;
    private int habitatCount;
    private int foodCount;
    private int staffCount;

    private Staff[] staffList;
    private Habitat[] habitats;
    private Food[] foods;
    private Animal[] animals;

    // Constructor
    public Zoo(String zooName, int animalCapacity, int habitatCapacity, int foodCapacity, int staffCapacity) {
        this.zooName = zooName;
        this.animals = new Animal[animalCapacity];
        this.animalCount = 0;
        this.habitats = new Habitat[habitatCapacity];
        this.habitatCount = 0;
        this.foods = new Food[foodCapacity];
        this.foodCount = 0;
        this.staffList = new Staff[staffCapacity];
        this.staffCount = 0;
    }

    public String getZooName() {
        return zooName;
    }

    public void removeAnimal(int animalID, Staff s) {
        if ( !s.getRole().equals("Manager")) return;
        if ( animalCount == 0 ) {
            System.out.println("Empty habitat");
            return;
        }
        for(int i=0;i<animalCount;i++) {
            if (animals[i] != null && animals[i].getAnimalID() == animalID) {
                for (int j = i; j < animalCount - 1; j++) {
                    animals[j] = animals[j + 1];
                }
                animals[animalCount-1] = null;
                animalCount--;
                System.out.println("Animal with ID " + animalID + " has been removed.");
                return;
            }
        }
        System.out.println("Animal not found.");
    }

    public void addAnimal(Animal animal, Staff s) {
        if (!s.getRole().equals("Manager")) return;
        if (animalCount >= 1) {
            for(Animal a : animals) {
                if (a.equals(animal)) {
                    System.out.println("Duplicate! Animal already exists in the list. Adding failed");
                    return;
                }
            }
        }
        if (animalCount < animals.length) {
            animals[animalCount] = animal;
            animalCount++;
            System.out.println("Animal added to the list.");
        }else {
            System.out.println("Habitat is full! Adding failed");
        }
    }

    @Override
    public String toString() {
        return "Zoo{" +
                "zooName='" + zooName + '\'' +
                ", animalCount=" + animalCount +
                ", habitatCount=" + habitatCount +
                ", foodCount=" + foodCount +
                ", staffCount=" + staffCount +
                '}';
    }
}