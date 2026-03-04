package com.zoo.models.habitat_types;

public class Forest extends Habitat {
    private String id;
    private static int nextId = 1;

    public Forest(Habitat h) {
        super(h.getName(), h.getFeedingTimes(), h.getFood());
        this.id = "Forest" + nextId;
        nextId++;
    }

    public String getId () {
        return id;
    }
}
