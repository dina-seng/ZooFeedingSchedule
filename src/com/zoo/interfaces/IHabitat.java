package com.zoo.interfaces;

import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import java.util.List;

public interface IHabitat {
    String getName();
    Food getFood();
    void addAnimal(Animal animal);
    void removeAnimal(Animal animal);   
    void addSchedule(Schedule schedule);
    void removeSchedule(Schedule schedule);
    void showAnimals();
    List<Schedule> getFeedingTimes();
    List<Animal> getAnimals();
}
