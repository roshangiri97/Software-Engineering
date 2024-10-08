package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ReportDisasterViewController {

    @FXML
    private ComboBox<String> disasterTypeComboBox;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> severityComboBox;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Label successLabel;  // Ensure this is added in FXML

    @FXML
    public void initialize() {
        // Populate ComboBox with pre-defined disaster types and severity levels
        disasterTypeComboBox.getItems().addAll("Hurricane", "Fire", "Earthquake", "Flood", "Medical", "Other");
        severityComboBox.getItems().addAll("Low", "Medium", "High", "Critical");

        // Ensure ComboBox defaults to the first option
        disasterTypeComboBox.setValue("Hurricane");
        severityComboBox.setValue("Medium");
    }

    @FXML
    protected void handleSubmitAction() {
        String disasterType = disasterTypeComboBox.getValue();
        String location = locationField.getText().trim();
        String severity = severityComboBox.getValue();
        String comments = commentsArea.getText().trim();

        // Validate that mandatory fields are not empty
        if (disasterType == null || location.isEmpty() || severity == null) {
            successLabel.setText("Error: Please fill in all the required fields.");
            successLabel.setStyle("-fx-text-fill: red;");  // Show error in red
        } else {
            saveDisasterReport(disasterType, location, severity, comments);
        }
    }

    private void saveDisasterReport(String disasterType, String location, String severity, String comments) {
        String query = "INSERT INTO disaster_reports (userId, DisasterType, Location, Severity, Comments, reportedTime, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Fetching current user information from session
            String userId = SessionManager.getInstance().getUserId();
            String reportedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String status = "Active";

            // Bind parameters
            stmt.setString(1, userId);
            stmt.setString(2, disasterType);
            stmt.setString(3, location);
            stmt.setString(4, severity);
            stmt.setString(5, comments);
            stmt.setString(6, reportedTime);
            stmt.setString(7, status);

            // Execute the query
            stmt.executeUpdate();

            // Show success message and clear fields
            successLabel.setText("Success: Disaster report submitted.");
            successLabel.setStyle("-fx-text-fill: green;");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            successLabel.setText("Error: Could not submit disaster report.");
            successLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void clearFields() {
        // Reset input fields after successful submission
        disasterTypeComboBox.setValue("Hurricane");
        locationField.clear();
        severityComboBox.setValue("Medium");
        commentsArea.clear();
    }

    @FXML
protected void handleCancelAction() {
    try {
        // Load the homepage view when the cancel button is pressed
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
        Parent root = loader.load();

        // Get the controller of the loaded homepage and initialize it with session data
        HomepageController homepageController = loader.getController();
        homepageController.initializePage();

        // Get the current stage (window) and set the homepage scene
        Stage stage = (Stage) disasterTypeComboBox.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error loading homepage view: " + e.getMessage());
    }
}


}
