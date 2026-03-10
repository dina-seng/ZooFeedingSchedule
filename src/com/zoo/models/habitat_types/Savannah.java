package com.zoo.models.habitat_types;

import com.zoo.models.Animal;
import com.zoo.models.Food;
import com.zoo.services.Schedule;
import java.util.List;

public class Savannah extends Habitat {
    private String id, name;
    private static int nextId = 1;

    public Savannah(List<Schedule> feedingTimes, Food food) {
        super(feedingTimes, food);
        this.name = "SV" + nextId++;
        this.id = "Savannah" + nextId++;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
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
