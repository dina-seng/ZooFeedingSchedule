package com.zoo.models.habitat_types;

import com.zoo.interfaces.IHabitat;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.services.Schedule;
import java.util.ArrayList;
import java.util.List;     

public abstract class Habitat implements IHabitat{
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
    @Override public List<Animal> getAnimals() { return new ArrayList<>(animals); }


    @Override
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    @Override
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    @Override
    public void addSchedule(Schedule schedule) {
        feedingTimes.add(schedule);
    }
    @Override
    public void removeSchedule(Schedule schedule) {
        feedingTimes.remove(schedule);
    }
    @Override
    public void showAnimals(){
        System.out.println("-- List of animals in " + getName() + " --");
        for (Animal a : getAnimals()) {
            System.out.println(a.getName() + getFood());
        }
    }

    // Abstract methods
    @Override
    public abstract String getName();
    public abstract String getId();
    public abstract void feedAnimals();             
    public abstract boolean canHouse(Animal animal);
}