package controller;

import model.entity.AppUsers;

public class Session {
    private static Session instance;
    private AppUsers currentUser;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setCurrentUser(AppUsers user) {
        this.currentUser = user;
    }

    public AppUsers getCurrentUser() {
        return currentUser;
    }

    public int getUserId() {
        return currentUser != null ? currentUser.getUser_id() : -1;
    }

    public String getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRole());
    }
    public boolean isTeacher() {
        return "TEACHER".equalsIgnoreCase(getRole());
    }
    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(getRole());
    }

    public void clear() {
        currentUser = null;
    }
}
