
public class Schedule {
    String staffId;
    String habitatId;
    String date;
    String time;

    Schedule(String staffId, String habitatId, String date, String time) {
        this.staffId = staffId;
        this.habitatId = habitatId;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Schedule [staffId=" + staffId + ", habitatId=" + habitatId + ", date=" + date + ", time=" + time + "]";
    }

}
