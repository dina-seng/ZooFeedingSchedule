package com.zoo.services;

import com.zoo.interfaces.IStaff;
import com.zoo.models.staff_roles.Keeper;

public class Schedule {

    private int id;
    private IStaff assignedKeeper;
    private String date;
    private String time;
    private boolean isCompleted;
    private static int nextId = 1;

    public Schedule(IStaff keeper, String date, String time) {
        this.id = nextId++;
        this.assignedKeeper = keeper;
        this.date = date;
        this.time = time;
        this.isCompleted = false;
    }

    public void completeTask() {
        isCompleted = true;
    }

    // Getters
    public int getId() { return id; }
    public IStaff getAssignedKeeper() { return assignedKeeper; }
    public String getDate() { return date; }
    public String getTime() { return time; }        
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setAssignedKeeper(Keeper keeper) { this.assignedKeeper = keeper; }
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }


    @Override
    public String toString() {
        return "Schedule: [" +
                "Keeper=" + assignedKeeper.getUsername() +
                ", Time=" + date + " " + time +
                ", Status=" + (isCompleted ? "Done" : "Pending") +
                "]";
    }
}