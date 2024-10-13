package com.disasterresponse.controller;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(ApplicationExtension.class)  // Enables TestFX for JUnit 5
public class LoginControllerTest  {

    private LoginController loginController;
    @Start
    
    public void start(Stage stage) throws Exception {
        // Initialize the controller manually (without calling start(Stage))
        loginController = new LoginController();
        // You can also initialize any other necessary components here if needed
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize the controller for each test
        loginController = new LoginController();
    }

    @Test
    public void testValidLogin(FxRobot robot) {
        // Simulate a valid login attempt
        boolean result = loginController.isValidLogin("admin", "admin123");
        assertTrue(result, "Login should succeed with valid credentials.");
    }

    @Test
    public void testInvalidLogin(FxRobot robot) {
        // Simulate an invalid login attempt
        boolean result = loginController.isValidLogin("testuser", "wrongpassword");
        assertFalse(result, "Login should fail with invalid credentials.");
    }
    
}
