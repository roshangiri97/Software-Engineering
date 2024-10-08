package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/users.csv";

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

        if (selectedRole == null) {
            errorMessageLabel.setVisible(true);
            errorMessageLabel.setText("Please select a role.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessageLabel.setVisible(true);
            errorMessageLabel.setText("Passwords do not match.");
            return;
        }

        try {
            appendUserToCSV(fullName, address, phone, username, email, password, selectedRole);
            loadLoginView();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessageLabel.setVisible(true);
            errorMessageLabel.setText("Error saving user data.");
        }
    }

    private void appendUserToCSV(String fullName, String address, String phone, String username, String email, String password, String accessLevel) throws IOException {
        File csvFile = new File(CSV_FILE_PATH);

        boolean fileExists = csvFile.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH, true))) {
            if (!fileExists) {
                writer.write("userId,FullName,Address,PhoneNumber,Username,Email,Password,AccessLevel");
                writer.newLine();
            }

            int userId = generateUserId();
            writer.write(userId + "," + fullName + "," + address + "," + phone + "," + username + "," + email + "," + password + "," + accessLevel);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private int generateUserId() {
        int maxUserId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails.length > 0) {
                    try {
                        int currentUserId = Integer.parseInt(userDetails[0].trim());
                        if (currentUserId > maxUserId) {
                            maxUserId = currentUserId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid userId format: " + userDetails[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxUserId + 1;
    }

    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading login view.");
        }
    }

    @FXML
    protected void handleBackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
