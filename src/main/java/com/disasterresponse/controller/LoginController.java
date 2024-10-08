package com.disasterresponse.controller;

import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;  // Added Label to show error messages

    private String userAccessLevel; // To store user's access level
    private String fullName; // To store user's full name
    private String userId; // To store user's ID

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/users.csv";

    @FXML
    protected void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (authenticate(username, password)) {
            // Store user session details
            SessionManager.getInstance().setLoggedInUser(userId, username, userAccessLevel, fullName);
            loadHomepageView();
        } else {
            errorMessageLabel.setText("Invalid username or password!!");
            errorMessageLabel.setVisible(true);  // Display error message
        }
    }

    public boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(CSV_FILE_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length >= 8) {
                    String storedUserId = userData[0]; // userId is at index 0
                    String storedUsername = userData[4]; // Username is at index 4
                    String storedPassword = userData[6]; // Password is at index 6
                    String storedAccessLevel = userData[7]; // AccessLevel is at index 7
                    String storedFullName = userData[1]; // FullName is at index 1
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        userId = storedUserId; // Store userId
                        userAccessLevel = storedAccessLevel; // Store access level
                        fullName = storedFullName; // Store full name
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loadHomepageView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Pass the access level to the HomepageController
            HomepageController controller = loader.getController();
            controller.initializePage(); // Initialize with session data

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading homepage view.");
        }
    }

    @FXML
    protected void handleSignupAction() {
        // Load the signup page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/SignupView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading signup page.");
        }
    }

    @FXML
    protected void handleForgotPasswordAction() {
        // Redirect to Forgot Password page
        System.out.println("Redirecting to forgot password page.");
    }
}
