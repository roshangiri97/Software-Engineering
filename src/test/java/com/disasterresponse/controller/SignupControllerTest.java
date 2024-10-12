package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;  // Import for cleanup
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)  // Enables TestFX for JUnit 5
public class SignupControllerTest {

    public SignupController signupController;

    @Start
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the SignupController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/SignupView.fxml"));
        Parent root = loader.load();
        signupController = loader.getController(); // Get the controller from the loaded FXML
        stage.setScene(new Scene(root));
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // This is optional, as controller is already initialized in @Start
    }

    @AfterEach
    public void tearDown() {
        // Call the method to delete test data after each test
        clearTestData();
    }

    @Test
    public void testValidSignup(FxRobot robot) {
        robot.clickOn(signupController.fullNameField).write("John Doe");
        robot.clickOn(signupController.addressField).write("123 Main St");
        robot.clickOn(signupController.phoneField).write("1234567890");
        robot.clickOn(signupController.usernameField).write("johndoe");
        robot.clickOn(signupController.emailField).write("john@example.com");
        robot.clickOn(signupController.passwordField).write("password123");
        robot.clickOn(signupController.confirmPasswordField).write("password123");
        robot.clickOn(signupController.roleComboBox);
        robot.clickOn("Public"); // Select the role

        // Call the signup action
        robot.clickOn("Register"); // Click the register button

        // Check if the error message label is not visible
        assertFalse(signupController.errorMessageLabel.isVisible(), "Error message should not be visible for valid signup.");
        clearTestData();
    }

    @Test
    public void testInvalidSignup(FxRobot robot) {
        robot.clickOn(signupController.fullNameField).write(""); // Empty full name
        robot.clickOn(signupController.addressField).write("123 Main St");
        robot.clickOn(signupController.phoneField).write("1234567890");
        robot.clickOn(signupController.usernameField).write("johndoe");
        robot.clickOn(signupController.emailField).write("john@example.com");
        robot.clickOn(signupController.passwordField).write("password123");
        robot.clickOn(signupController.confirmPasswordField).write("differentpassword"); // Different password
        robot.clickOn(signupController.roleComboBox);
        robot.clickOn("Public"); // Select the role

        // Call the signup action
        robot.clickOn("Register"); // Click the register button

        // Check if the error message label is visible and contains the correct message
        assertTrue(signupController.errorMessageLabel.isVisible(), "Error message should be visible for invalid signup.");
        assertEquals("Please fill in all the required fields and match passwords.", signupController.errorMessageLabel.getText());
        clearTestData();
    }

    public void clearTestData() {
        // Implement your logic to delete test data from the database here.
        // Example: Execute a DELETE query to remove the test user from the users table
        String query = "DELETE FROM users WHERE Username = 'johndoe'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
