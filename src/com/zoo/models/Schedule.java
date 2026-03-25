package com.zoo.models;

import com.zoo.dao.ScheduleDAO;

public class Schedule {

    private int id;                   
    private int animalId;
    private int assignedKeeper;
    private int foodId;
    private String feedingTime;
    private float quantityKg;
    private String notes;
    private boolean isCompleted;

    public Schedule(int id, int animalId, int staffid, int foodId, String feedingTime,
                    float quantityKg, String notes, boolean isCompleted) {
        this.id = id;
        this.animalId = animalId;
        this.assignedKeeper = staffid;
        this.foodId = foodId;
        this.feedingTime = feedingTime;
        this.quantityKg = quantityKg;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    // Mark as completed
    public void markCompleted() {
        this.isCompleted = true;
        try {
            ScheduleDAO.updateCompletionStatus(id, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters
    public int getId() { return id; }
    public int getAnimalId() { return animalId; }
    public int getAssignedKeeper() { return assignedKeeper; }
    public int getFoodId() { return foodId; }
    public String getFeedingTime() { return feedingTime; }
    public float getQuantityKg() { return quantityKg; }
    public String getNotes() { return notes; }
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setFeedingTime(String feedingTime) { this.feedingTime = feedingTime; }
    public void setQuantityKg(float quantityKg) { this.quantityKg = quantityKg; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCompleted(boolean completed) { this.isCompleted = completed; }

    @Override
    public String toString() {
        return String.format("Schedule[id=%d, animalId=%d, keeper=%s, foodId=%d, time=%s, day=%s, qty=%.2f, status=%s]",
                id, animalId, assignedKeeper, foodId, feedingTime, quantityKg,
                isCompleted ? "Done" : "Pending");
    }
}