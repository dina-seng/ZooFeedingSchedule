class Animal {
    private int animalID;
    private String name;
    private int age;
    private Habitat habitat;
    private String species;
    private double weight;

    
    public Animal(int animalID, String name, int age, Habitat habitat, String species, double weight) {
        this.animalID = animalID;
        this.name = name;
        this.age = age;
        this.habitat = habitat;
        this.species = species;
        this.weight = weight;
    } 

    public String getName() { return name; } 
    public int getAnimalID() { return animalID; }
    public int getAge() { return age; }
    public Habitat getHabitat() { return habitat; }
    public String getSpecies() { return species; }
    public double getWeight() { return weight; }


    public void setAnimalID(int animalID) {
        if (animalID > 0) {
            this.animalID = animalID;
        } else {
            System.out.println("Animal ID must be positive.");
        }
    }


    public void setWeight(double weight) {
        if (weight >= 0) {
            this.weight = weight;
        } else {
            System.out.println("Weight cannot be negative.");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Animal{");
        sb.append("animalID=").append(animalID);
        sb.append(", name=").append(name);
        sb.append(", age=").append(age);
        sb.append(", habitat=").append(habitat);
        sb.append(", species=").append(species);
        sb.append(", weight=").append(weight);
        sb.append('}');
        return sb.toString();
    }


    
}