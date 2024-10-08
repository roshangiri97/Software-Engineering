package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

        saveUser(fullName, address, phone, username, email, password, selectedRole);
    }

    private void saveUser(String fullName, String address, String phone, String username, String email, String password, String accessLevel) {
        String query = "INSERT INTO users (FullName, Address, PhoneNumber, Username, Email, Password, AccessLevel) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, username);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.setString(7, accessLevel);

            stmt.executeUpdate();

            loadLoginView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
            Parent root = loader.load();
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
