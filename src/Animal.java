class Animal {
    int animalID;
    String name;
    int age;
    Habitat habitat;
    String species;

    Animal(int animalID, String name, int age, Habitat habitat, String species) {
        this.animalID = animalID;
        this.name = name;
        this.age = age;
        this.habitat = habitat;
        this.species = species;
    }

    @Override
    public String toString() {
        return "Animal  animalID=" + animalID + ", name=" + name + ", age=" + age + ", habitat=" + habitat + ", species=" + species
                + "]";
    }
}