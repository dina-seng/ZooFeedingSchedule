public class Staff {
    private String staffID;
    private String name;
    private String role;
    private double salary; 
    private int yearOfExperience;
    private Habitat assignHabitat; 

    public Staff(String staffID, String name, String role, double salary, int yearOfExperience) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.yearOfExperience = yearOfExperience; 
    }

    public String getName(){return name;}
    public String getRole(){return role;}
    public String getStaffID(){return staffID;}
    public double getSalary(){return salary;}
    public int getYearOfExperience(){return yearOfExperience;}
    public Habitat getAssignHabitat(){return assignHabitat;}


    public void setName(String name) {this.name = name;}
    public void setRole(String role) {this.role = role;}

    public void setAssignHabitat(Habitat habitat) {this.assignHabitat = habitat;}

    public void setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
        } else {
            System.out.println("Salary must be positive.");
        }
    }


    @Override
    public String toString() {
        return "Staff [staffID=" + staffID + ", name=" + name + ", role=" + role  + "]";
    }

}