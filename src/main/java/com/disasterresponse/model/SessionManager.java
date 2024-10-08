package com.disasterresponse.model;

public class SessionManager {

    private static SessionManager instance;

    private String userId;
    private String username;
    private String userAccessLevel;
    private String fullName;

    // Private constructor to prevent instantiation
    private SessionManager() {}

    // Singleton pattern: ensures only one instance of SessionManager exists
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Method to set the logged-in user details
    public void setLoggedInUser(String userId, String username, String userAccessLevel, String fullName) {
        this.userId = userId;
        this.username = username;
        this.userAccessLevel = userAccessLevel;
        this.fullName = fullName;
    }

    // Method to clear the session
    public void clearSession() {
        this.userId = null;
        this.username = null;
        this.userAccessLevel = null;
        this.fullName = null;
    }

    // Getters for the session details
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserAccessLevel() {
        return userAccessLevel;
    }

    public String getFullName() {
        return fullName;
    }

    // Check if a user is logged in
    public boolean isLoggedIn() {
        return userId != null;
    }
}
