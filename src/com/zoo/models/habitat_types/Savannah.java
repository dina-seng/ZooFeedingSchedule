package com.zoo.models.habitat_types;

public class Savannah extends Habitat {
    private String id;
    private static int nextId = 1;

    public Savannah(Habitat h) {
        super(h.getName(), h.getFeedingTimes(), h.getFood());
        this.id = "Savannah" + nextId;
        nextId++;
    }

    public String getId () {
        return id;
    }
    
}
