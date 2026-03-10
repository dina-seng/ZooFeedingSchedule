package com.zoo.models.staff_roles;
import com.zoo.interfaces.IHabitat;

public class Manager extends Staff {
    private float salary;

    @Override
    public boolean can(String action) {
        return true;
    }

    @Override
    public boolean canAccessHabitat(IHabitat habitat) {
        return true;
    }

    @Override
    public void assignHabitat(IHabitat habitat) {
        throw new UnsupportedOperationException("Managers cannot be assigned habitats.");
    }

    // Constructor
    public Manager(String id, String name, String username, String password, float salary) {
        super(id, name, username, password);
        setSalary(salary);
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
        return getId().equals(other.getId());
    }

}
