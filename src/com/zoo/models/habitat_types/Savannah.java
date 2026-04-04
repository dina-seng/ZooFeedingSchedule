package com.zoo.models.habitat_types;

import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.models.Schedule;
import java.util.List;

public class Savannah extends Habitat {


    public Savannah(List<Schedule> feedingTimes, Food food) {
        super(feedingTimes, food);

    }

   
    @Override
    public void feedAnimals() {
        for (Animal a : getAnimals()) {
            System.out.println("  → " + a.getName() + " (" + a.getSpecies() + ") is eating.");
        }
    }

    @Override
    public boolean canHouse(Animal animal) {
    String species = animal.getSpecies().toLowerCase();
        return species.contains("zebra") ||
               species.contains("lion")   ||
               species.contains("hyena")   ||
               species.contains("giraffe")  ||
               species.contains("cheetah");
    }
}
