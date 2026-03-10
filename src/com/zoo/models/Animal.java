package com.zoo.models;

public class Animal {
    private int id, age;
    private String name;
    private String species;
    private double weight;
    private static int nextId = 1;

    public Animal(String name, int age, String species, double weight) {
        this.id = nextId;
        nextId++;
        setName(name);
        setAge(age);
        setSpecies(species);
        setWeight(weight);
    }

    // Getters
    public String getName() { return name; } 
    public int getId() { return id; }
    public int getAge() { return age; }
    public String getSpecies() { return species; }
    public double getWeight() { return weight; }
    public static int getNextId() { return nextId;}

    // Helpers
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Setters
    public void setName(String name) {
        if (isBlank(name)) this.name = "Unknown";
        else this.name = name;
    }

    public void setAge(int age) {
        this.age = (age > 0) ? age : 0;
    }

    public void setSpecies(String species) {
        if (isBlank(species)) this.species = "Unknown";
        else this.species = species;
    }

    public void setWeight(double weight) {
        if ( weight <= 0 ) this.weight = 0;
        else this.weight = weight;
    }

    public boolean matchesSpeciesIgnoreCase(String species) {
        return this.species != null && this.species.equalsIgnoreCase(species);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
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