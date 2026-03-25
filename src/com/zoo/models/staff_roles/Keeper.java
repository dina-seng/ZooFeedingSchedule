package com.zoo.models.staff_roles;

import com.zoo.models.habitat_types.Habitat;
import com.zoo.services.Zoo;
import java.util.ArrayList;
import java.util.List;

public class Keeper extends Staff {
    private float salary;
    private List<Habitat> assignedHabitats = new ArrayList<>();

    @Override
    public boolean can(String action) {
        return action.equals(Zoo.ANIMAL_MANAGE) || action.equals(Zoo.VIEW_REPORT) || action.equals(Zoo.HABITAT_MANAGE);
    }

    // Constructor
    public Keeper(int id, String name, String username, String password, float salary) {
        super(id, name, username, password);
        setSalary(salary);
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        if(salary < 300)
        {
            System.out.println("error: more salary expected");
        }else
        {
            this.salary = salary;
        }
    }

    @Override
    public boolean canAccessHabitat(Habitat habitat) {
        return assignedHabitats.contains(habitat);
    }

    @Override
    public void assignHabitat(Habitat habitat) {
        if (!assignedHabitats.contains(habitat)) {
            assignedHabitats.add(habitat);
        }
    }

    @Override
    public String toString() {
        return "Keeper [salary=" + salary + ", getSalary()=" + getSalary() + ", getId()=" + getId()
                + ", getUsername()=" + getUsername() + ", getName()=" + getName() + ", isActive()=" + isActive()
                + ", toString()=" + super.toString() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Keeper other)) return false;

        return getId() == other.getId();
    }
}

