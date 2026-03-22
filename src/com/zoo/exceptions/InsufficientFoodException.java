package com.zoo.exceptions;

public class InsufficientFoodException extends ZooException {
    public InsufficientFoodException(String foodName, double needed, double available) {
        super("Insufficient food: " + foodName + ". Needed: " + needed + "kg, Available: " + available + "kg.");
    }

}
