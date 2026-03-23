package com.zoo.models.habitat_types;

import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.services.Schedule;
import java.util.List;

public class Ocean extends Habitat{

    public Ocean(List<Schedule> feedingTimes, Food food) {
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
        return species.contains("fish") ||
               species.contains("shark") ||
               species.contains("penguin") ||
               species.contains("turtle") ||
               species.contains("octopus");
    }
}
