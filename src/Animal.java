import java.util.Objects;

public class Animal {
    private int id;
    private String name;
    private int age;
    private String species;
    private double weight;
    private static int nextId = 1;

    public Animal(String name, int age, String species, double weight) {
        this.id = nextId;
        nextId++;
        this.name = name;
        this.age = age;
        this.species = species;
        this.weight = weight;
    }

    public String getName() { return name; } 
    public int getAnimalID() { return id; }
    public int getAge() { return age; }
    public String getSpecies() { return species; }
    public double getWeight() { return weight; }
    public static int getNextId() { return nextId;}

    public void setAge(int age) {
        if (age <= 0) {
            return;
        }
         this.age = age;
    }

    public boolean matchesSpecies(String species) {
        return this.species.equals(species);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id && age == animal.age && Double.compare(weight, animal.weight) == 0
                && Objects.equals(name, animal.name) && Objects.equals(species, animal.species);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", species='" + species + '\'' +
                ", weight=" + weight +
                '}' + "\n";
    }
}