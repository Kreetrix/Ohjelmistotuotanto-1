package controller;

import model.entity.AppUsers;

/**
 * Singleton class for managing session information, including the current user and their role.
 */
public class Session {
    private static Session instance;
    private AppUsers currentUser;

    private Session() {}

    /**
     * Returns the singleton instance of the session. If no session exists, a new one is created.
     *
     * @return the singleton Session instance
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Sets the current user for this session.
     *
     * @param user the user to set as the current user
     */
    public void setCurrentUser(AppUsers user) {
        this.currentUser = user;
    }

    /**
     * Returns the current user associated with this session.
     *
     * @return the current user, or null if no user is set
     */
    public AppUsers getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the user ID of the current user.
     *
     * @return the user ID, or -1 if no user is set
     */
    public int getUserId() {
        return currentUser != null ? currentUser.getUser_id() : -1;
    }

    /**
     * Returns the role of the current user.
     *
     * @return the user's role, or null if no user is set
     */
    public String getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * Checks if the current user has the admin role.
     *
     * @return true if the current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(getRole());
    }

    /**
     * Checks if the current user has the teacher role.
     *
     * @return true if the current user is a teacher, false otherwise
     */
    public boolean isTeacher() {
        return "TEACHER".equalsIgnoreCase(getRole());
    }

    /**
     * Checks if the current user has the student role.
     *
     * @return true if the current user is a student, false otherwise
     */
    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(getRole());
    }

    /**
     * Clears the current user from the session.
     */
    public void clear() {
        currentUser = null;
    }
}
