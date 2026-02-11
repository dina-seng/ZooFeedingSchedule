public class Schedule {
    private Staff assignedStaff;
    private String date;
    private String time;
    private boolean isCompleted = false;
    private String feedingNotes;

    public Schedule(Staff staff, String date, String time) {
        this.assignedStaff = staff;
        this.date = date;
        this.time = time;
        this.feedingNotes = "Standard Feeding";
    }

    public Schedule(Staff staff, String date, String time,String Note) {
        this.assignedStaff = staff;
        this.date = date;
        this.time = time;
        this.feedingNotes = Note;
    }

    public String getFeedingNotes() { return feedingNotes; }
    public String getDate() { return date;}
    public Staff getAssignedStaff() { return assignedStaff; }
    public String getTime() { return time; }
    public boolean isCompleted() { return isCompleted; }


    public void Completed() {
        this.isCompleted = true;
    }

    public void setFeedingNotes(String notes) {
        if (notes != null) {
            this.feedingNotes = notes;
        }else {
            System.out.println("Feed notes is invalid");
        }
    }
    
    @Override
    public String toString() {
        return "Feeding Schedule [" +
               "Staff=" + assignedStaff.getName() + 
               ", Time=" + date + " " + time +
               ", Status=" + (isCompleted ? "Done" : "Pending") + "]";
    }
}