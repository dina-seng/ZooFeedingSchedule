package com.zoo.models.staff_roles;

public class Manager extends Staff {
    private float salary;

    @Override
    public boolean can(String action) {
        // TODO Auto-generated method stub
        return true;
    }

    // Constructor
    public Manager(Staff s, float salary) {
        
        super(s.getId(), s.getName(), s.getUsername(), s.getPassword());
        this.setSalary(salary);
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
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Manager other = (Manager) obj;
        if (Float.floatToIntBits(salary) != Float.floatToIntBits(other.salary))
            return false;
        return true;
    }

}
