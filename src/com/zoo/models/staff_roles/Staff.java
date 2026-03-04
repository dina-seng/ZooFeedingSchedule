package com.zoo.models.staff_roles;
import com.zoo.interfaces.IStaff;
import java.util.Objects;

public class Staff implements IStaff{
    private String id, name, username, password;
    private boolean status;

    // Constructor
    public Staff(String id, String name,String username, String password) {
        setId(id);
        setName(name);
        setUsername(username);
        setPassword(password);
        this.status = true;
    }

    protected String getPassword() { return password; }

    // Getters
    public String getId(){ return id; }
    public String getUsername(){return username;}
    public String getName(){return name;}
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
        if(isBlank(id)) this.id = "Unknown";
        else this.id = id.trim();
    }

    public void setName(String name) {
        if(isBlank(name)) this.name = "NA";
        else this.name = name.trim();
    }

    public void setUsername(String username) {
        if(isBlank(username)) this.username = "NA";
        else this.username = username;
    }

    public void setPassword(String password) {
        String pw = (password == null) ? "" : password;
        if (pw.length() < 4) this.password = "0000";
        else this.password = pw;
    }

    public void setStatus(boolean stutus) {
        this.status = stutus;
    } 

    @Override
    public String toString() {
        return "Staff [id=" + id + ", name=" + name + ", username=" + username + ", password=" + password
                +", status=" + status + "]";
    }

    @Override
    public boolean can(String action) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final Staff other = (Staff) obj;
        return Objects.equals(this.id, other.id);
    }

}
