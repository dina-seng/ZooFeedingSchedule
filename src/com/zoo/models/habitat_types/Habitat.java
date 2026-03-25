package com.zoo.models.habitat_types;

import com.zoo.interfaces.IHabitat;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import java.util.ArrayList;
import java.util.List;

public abstract class Habitat implements IHabitat {

    private int id;
    private String name;
    private String location;
    private int capacity;
    private Food food;
    protected List<Schedule> feedingTimes = new ArrayList<>();
    protected List<Animal> animals;

    protected Habitat(List<Schedule> feedingTimes, Food food) {
        this.feedingTimes = (feedingTimes != null) ? new ArrayList<>(feedingTimes) : new ArrayList<>();
        this.food = food;
        this.animals = new ArrayList<>();
    }

    // Getters
    @Override public List<Schedule> getFeedingTimes() { return new ArrayList<>(feedingTimes); }
    @Override public Food getFood() { return food; }
    @Override public List<Animal> getAnimals() { return animals; } 
    public String getId()       { return String.valueOf(this.id); }
    public String getName()     { return this.name; }
    public String getLocation() { return this.location; }
    public int getCapacity()    { return this.capacity; }


    // Setters
    public void setId(int id)           { this.id = id; }
    public void setName(String name)    { this.name = name; }
    public void setLocation(String loc) { this.location = loc; }
    public void setCapacity(int cap)    { this.capacity = cap; }

    public double getFeedingPerformance() {
        if (this.feedingTimes == null || this.feedingTimes.isEmpty()) return 0.0;
        long completed = this.feedingTimes.stream()
                .filter(Schedule::isCompleted).count();
        double performance = ((double) completed / this.feedingTimes.size()) * 100;
        return Math.round(performance * 100.0) / 100.0;
    }

    @Override public void addAnimal(Animal animal)       { animals.add(animal); }
    @Override public void removeAnimal(Animal animal)    { animals.remove(animal); }
    @Override public void addSchedule(Schedule schedule) { feedingTimes.add(schedule); }
    @Override public void removeSchedule(Schedule s)     { feedingTimes.remove(s); }

    @Override
    public void showAnimals() {
        System.out.println("-- List of animals in " + getName() + " --");
        for (Animal a : getAnimals()) {
            System.out.println(a.getName() + getFood());
        }
    }

    public abstract void feedAnimals();
    public abstract boolean canHouse(Animal animal);
}