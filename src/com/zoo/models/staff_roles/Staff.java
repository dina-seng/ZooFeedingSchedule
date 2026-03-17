package com.zoo.models.staff_roles;
import com.zoo.interfaces.IStaff;
import com.zoo.models.habitat_types.Habitat;
import java.util.Objects;

@FunctionalInterface
interface HabitatAccess {
    boolean canAccessHabitat(Habitat habitat);
}

public abstract class Staff implements IStaff, HabitatAccess{
    private String id, name, username, password;
    private boolean status;

    // Constructor
    public Staff(String id, String name, String username, String password) {
        this.id = validateId(id);
        this.name = validateName(name);
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.status = true;
    }

    protected String getPassword() { return password; }

    // Getters
    @Override
    public String getId(){ return id; }
    @Override
    public String getUsername(){return username;}
    @Override
    public String getName(){return name;}
    @Override
    public boolean isActive() { return status; }

    // Checking password
    @Override
    public boolean checkPassword(String input) {
        return this.password != null && this.password.trim().equals(input.trim());
    }

    // Helpers
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Setters
    public void setId(String id) {
        this.id = validateId(id);
    }

    public void setName(String name) {
        this.name = validateName(name);
    }

    public void setUsername(String username) {
        this.username = validateUsername(username);
    }

    public void setPassword(String password) {
        this.password = validatePassword(password);
    }

    public void setStatus(boolean status) {
        this.status = status;
    } 

    @Override
    public String toString() {
        return "Staff [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password
                +", status=" + status + "]";
    }

    @Override
    public abstract boolean can(String action);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Staff other = (Staff) obj;
        return Objects.equals(this.id, other.id);
    }

    private String validateId(String id) {
        return isBlank(id) ? "Unknown" : id.trim();
    }

    private String validateName(String name) {
        return isBlank(name) ? "NA" : name.trim();
    }

    private String validateUsername(String username) {
        return isBlank(username) ? "NA" : username;
    }

    private String validatePassword(String password) {
        String pw = (password == null) ? "" : password;
        if (pw.isEmpty()) throw new IllegalArgumentException("Password required");
        if (pw.length() < 8) throw new IllegalArgumentException("Password must be at least 8 characters");
        pw = pw.trim();
        return pw;
    }

}
