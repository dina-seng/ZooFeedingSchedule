package com.zoo.models.habitat_types;

import com.zoo.interfaces.IHabitat;
import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.services.Schedule;
import java.util.ArrayList;
import java.util.List;     

public class Habitat implements IHabitat{
    private String name;
    private Food food;
    private List<Schedule> feedingTimes = new ArrayList<>();
    private List<Animal> animals;

    public Habitat(String name, List<Schedule> feedingTimes, Food food) {
        this.name = name;
        this.feedingTimes = feedingTimes;
        this.food = food;
        this.animals = new ArrayList<>();
    }
    
    // Helpers
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Getters
    public String getName() { return name; }
    public List<Animal> getAnimals() {
        return animals;
    }
    public List<Schedule> getFeedingTimes() {
        return feedingTimes;
    }
    public Food getFood() {
        return food;
    }

    // Setters 
    public void setName(String name) {
        if(isBlank(name)) this.name = "Unknown";
        else this.name = name;
    }

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

    public List<Schedule> getfeedingTimes() {
        return feedingTimes;
    }

}