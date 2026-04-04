package com.zoo.models.staff_roles;
import com.zoo.models.habitat_types.Habitat;
import java.util.List;

public class Manager extends Staff {
    private float salary;
    
    // Constructor
    public Manager(int id, String name, String username, String password, float salary) {
        super(id, name, username, password);
        setSalary(salary);
    }


    public void performDailyFeeding(List<Habitat> habitats) {
        try {
            System.out.println("Opening food storage...");
            
            for (Habitat h : habitats) {
                h.feedAnimals();
            }

        } catch (NullPointerException e) {
            System.out.println("Error: One of the habitats is not properly initialized!");
        } finally {
            System.out.println("Feeding session logged and storage secured.");
        }
    }


    @Override
    public boolean can(String action) {
        return true;
    }

    @Override
    public boolean canAccessHabitat(Habitat habitat) {
        return true;
    }

    @Override
    public void assignHabitat(Habitat habitat) {
        throw new UnsupportedOperationException("Managers cannot be assigned habitats.");
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        if(salary < 1000)
        {
            System.out.println("error: more salary expected");
        }else
        {
            this.salary = salary;
        }
    }

    @Override
    public String toString() {
        return "Manager [salary=" + salary + ", getSalary()=" + getSalary() + ", getId()=" + getId()
                + ", getUsername()=" + getUsername() + ", getName()=" + getName() + ", isActive()=" + isActive()
                + ", toString()=" + super.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Manager)) return false;

        Manager other = (Manager) obj;
        return getId() == other.getId();
    }

}
