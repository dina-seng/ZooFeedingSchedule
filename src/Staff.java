import java.util.Objects;

public class Staff {
    private String id;
    private String name;
    private String role;
    private double salary; 
    private int yearOfExperience;
    private Habitat assignedHabitat;

    public Staff(String id, String name, String role, double salary, int yearOfExperience) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.yearOfExperience = yearOfExperience;
    }

    // Initialize normal staff require manager
    public Staff(String id, String name, String role, double salary, int yearOfExperience, Staff manager) {
        this.id = id;
        this.name = name;
        setRole(role,manager);
        setSalary(salary,manager);
        setAssignedHabitat(assignedHabitat,manager);
        this.yearOfExperience = yearOfExperience;
    }

    public String getName(){return name;}
    public String getRole(){return role;}
    public String getStaffID(){return id;}
    public double getSalary(){return salary;}
    public int getYearOfExperience(){return yearOfExperience;}
    public Habitat getAssignedHabitat(){return assignedHabitat;}

    public void setRole(String role,Staff s) {
        if (!s.getRole().equals("Manager")) return;
        if (role != null) {
            this.role = role;
        }else {
            System.out.println("Role is invalid");
        }
    }
    
    public void setAssignedHabitat(Habitat habitat, Staff s) {
        if (!s.getRole().equals("Manager")) return;
        if (habitat != null) {
            this.assignedHabitat = habitat;
        }else{
            System.out.println("Habitat is invalid");
        }
    }

    public void setSalary(double salary, Staff s) {
        if (!s.getRole().equals("Manager")) return;
        if (salary > 0) {
            this.salary = salary;
        } else {
            System.out.println("Salary must be positive.");
        }
    }

    public void setYearOfExperience(int yearOfExperience) {
        if (yearOfExperience > 0) {
            this.yearOfExperience = yearOfExperience;
        } else {
            System.out.println("Year of Experience is invalid.");
        }
    }

    @Override
    public String toString() {
        return "Staff [StaffID=" + id + ", name=" + name + ", role=" + role  + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return Objects.equals(role, staff.role);
    }

}