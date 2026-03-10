package com.zoo.models;
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

    public void setStock(double stock) {
        if (stock >= 0) {
            this.stock = stock;
        } else {
            System.out.println("Stock cannot be negative.");
        }
    }

    public void setCostPerUnit(double costPerUnit) {
        if (costPerUnit >= 0) {
            this.costPerUnit = costPerUnit;
        }else {
            System.out.println("Cost per unit must be positive.");
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