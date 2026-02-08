public class Schedule {
    // 1. Private Fields (Encapsulation)
    private Staff assignedStaff;   // Relationship: Who is feeding
    private Habitat targetHabitat; // Relationship: Where are they feeding
    private String date;
    private String time;
    private boolean isCompleted;   // Field 5: Tracking task status
    private String feedingNotes;   // Field 6: For special instructions

    // Constructor
    public Schedule(Staff staff, Habitat habitat, String date, String time) {
        this.assignedStaff = staff;
        this.targetHabitat = habitat;
        this.date = date;
        this.time = time;
        this.isCompleted = false; // Default to not finished
        this.feedingNotes = "Standard Feeding";
    }

    // 2. Getters (Access Control)
    public Staff getAssignedStaff() { return assignedStaff; }
    public Habitat getTargetHabitat() { return targetHabitat; }
    public String getTime() { return time; }
    public boolean isCompleted() { return isCompleted; }

    // 3. Setters (Validation)
    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    public void setFeedingNotes(String notes) {
        if (notes != null) {
            this.feedingNotes = notes;
        }
    }
    
    @Override
    public String toString() {
        return "Feeding Schedule [" +
               "Staff=" + assignedStaff.getName() + 
               ", Habitat=" + targetHabitat.getHabitatID() + 
               ", Time=" + date + " " + time + 
               ", Status=" + (isCompleted ? "Done" : "Pending") + "]";
    }
}