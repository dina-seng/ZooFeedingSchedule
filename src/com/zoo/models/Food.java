package com.zoo.models;


import com.zoo.exceptions.ZooException;

public class Food {
    private int id;
    private String name;
    private double stock;
    private String expiryDate;
    private double costPerUnit;
    private static int nextId = 1;

    public Food(String name, double stock, String expiryDate, double costPerUnit) {
        this.id = nextId++;
        this.name = name;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.costPerUnit = costPerUnit;
    }

    public int getId() { return id; }
    public double getStock() {return stock; }
    public String getName() { return name;}
    public String getExpiryDate() { return expiryDate; }
    public double getCostPerUnit() { return costPerUnit; }
    public static int getNextId(){ return nextId; }

    public void setStock(double stock) throws ZooException{
        if (stock >= 0) {
            this.stock = stock;
        } else {
            // System.out.println("Stock cannot be negative.");
            throw new ZooException("Inventory Error: "+ this.name +" cannot be negative.");
        }
    }

    public void setId(int id) {
        this.id = id;
     }

     public void setName(String name) {
        this.name = name;
    }

    public void setCostPerUnit(double costPerUnit)  throws ZooException{
        if (costPerUnit >= 0) {
            this.costPerUnit = costPerUnit;
        }else {
            // System.out.println("Cost per unit must be positive.");
            throw new ZooException("Pricing Error: "+ this.name +" cost per unit cannot be negative.");
        }
    }

    @Override
    public String toString() {
            return "Food{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", stock=" + stock +
                    ", expiryDate='" + expiryDate + '\'' +
                    ", costPerUnit=" + costPerUnit +
                    '}';
        }
}