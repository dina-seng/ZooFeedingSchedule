package com.zoo.services;

import com.zoo.staff_interface.IStaff; // Import the interface

public class Schedule {
    private IStaff assignedStaff; 
    private String date;
    private String time;
    private boolean isCompleted = false;
    private String feedingNotes;

    public Schedule(IStaff staff, String date, String time) {
        this.assignedStaff = staff;
        this.date = date;
        this.time = time;
        this.feedingNotes = "Standard Feeding";
    }

    public Schedule(IStaff staff, String date, String time, String note) {
        this.assignedStaff = staff;
        this.date = date;
        this.time = time;
        this.feedingNotes = note;
    }

    // Getters
    public IStaff getAssignedStaff() { return assignedStaff; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isCompleted() { return isCompleted; }

    public void completeTask() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        // Use getUsername() from the interface
        return "Feeding Schedule [" +
               "Staff=" + assignedStaff.getUsername() + 
               ", Time=" + date + " " + time +
               ", Status=" + (isCompleted ? "Done" : "Pending") + "]";
    }
}