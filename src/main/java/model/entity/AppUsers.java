package model.entity;

import java.sql.Timestamp;
/**
 * Entity class representing an application user.
 * <p>
 * Contains user information such as username, email, password hash, role, activity status, and creation timestamp.
 */

public class AppUsers {
    private int user_id, is_active;
    private String username, email, password_hash, role;
    private Timestamp created_at;

    public AppUsers(String username, String email, String password_hash, String role, int is_active, Timestamp created_at) {
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.role = role;
        this.is_active = is_active;
        this.created_at = created_at;
    }

    // Getters and Setters
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword_hash() {
        return password_hash;
    }
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public int getIs_active() {
        return is_active;
    }
    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "AppUsers{" +
                "user_id=" + user_id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", is_active=" + is_active +
                ", created_at=" + created_at +
                '}';
    }
}
