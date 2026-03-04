package com.zoo.models.habitat_types;

public class Ocean extends Habitat{
    private String id;
    private static int nextId = 1;

    public Ocean(Habitat h) {
        super(h.getName(), h.getFeedingTimes(), h.getFood());
        this.id = "Ocean" + nextId;
        nextId++;
    }

    public String getId () {
        return id;
    }
}
