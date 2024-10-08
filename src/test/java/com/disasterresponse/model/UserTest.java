package com.disasterresponse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a User object before each test
        user = new User("1", "john_doe", "password123", "Admin");
    }

    @Test
    public void testGetUserId() {
        assertEquals("1", user.getUserId());
    }

    @Test
    public void testGetUsername() {
        assertEquals("john_doe", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testGetRole() {
        assertEquals("Admin", user.getRole());
    }

    @Test
    public void testSetRole() {
        user.setRole("Emergency Responder");
        assertEquals("Emergency Responder", user.getRole());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("jane_doe");
        assertEquals("jane_doe", user.getUsername());
    }
}
