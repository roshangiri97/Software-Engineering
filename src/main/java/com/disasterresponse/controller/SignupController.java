package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignupController {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Label errorMessageLabel;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Agencies or Organization", "Emergency Responders", "Public");
    }

    @FXML
    protected void handleSignupAction() {
        String fullName = fullNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (selectedRole == null || !password.equals(confirmPassword)) {
            errorMessageLabel.setVisible(true);
            errorMessageLabel.setText("Please fill in all the required fields and match passwords.");
            return;
        }

        saveUser (fullName, address, phone, username, email, password, selectedRole);
    }

    private void saveUser (String fullName, String address, String phone, String username, String email, String password, String accessLevel) {
        String query = "INSERT INTO users (FullName, Address, PhoneNumber, Username, Email, Password, AccessLevel) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String hashedPassword = hash256(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, username);
            stmt.setString(5, email);
            stmt.setString(6, hashedPassword);
            stmt.setString(7, accessLevel);

            stmt.executeUpdate();

            loadLoginView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String hash256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle algorithm not found error
        }
        return null;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
            Parent root = loader.load();
            // Clear the previous user's details from the SessionManager
    SessionManager.getInstance().clearSession();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackAction() {
        loadLoginView();
    }
}