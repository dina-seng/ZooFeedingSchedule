public class Staff {
    String staffID;
    String name;
    String role;
    boolean morningShift = true;

    Staff(String staffID, String name, String role) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff [staffID=" + staffID + ", name=" + name + ", role=" + role  + "]";
    }

}