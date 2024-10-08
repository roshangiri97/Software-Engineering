package com.disasterresponse.model;

public class SessionManager {

    private static SessionManager instance;
    private String userId;
    private String username;
    private String userRole;
    private String fullName;

    private SessionManager() {
        // Private constructor to enforce singleton pattern
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInUser(String userId, String username, String userRole, String fullName) {
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.fullName = fullName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getFullName() {
        return fullName;
    }

    public void clearSession() {
        this.userId = null;
        this.username = null;
        this.userRole = null;
        this.fullName = null;
    }
}
