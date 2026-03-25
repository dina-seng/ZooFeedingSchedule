package com.zoo.exceptions;

public class InvalidNameException extends ZooException {
    public InvalidNameException(String fieldName, String value) {
        super("Invalid " + fieldName + ": \"" + value + "\" — names cannot be numbers or blank.");
    }
}