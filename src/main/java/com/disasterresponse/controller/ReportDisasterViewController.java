package com.disasterresponse.controller;

<<<<<<< HEAD
import com.disasterresponse.model.Disaster;
=======
import com.disasterresponse.model.DatabaseConnection;
>>>>>>> IncidentReport
import com.disasterresponse.model.SessionManager;
import dao.DisasterDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    private DisasterDAO disasterDAO = new DisasterDAO(); // DAO to handle database operations

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
        // Get the currently logged-in user's ID from SessionManager
        String userId = SessionManager.getInstance().getUserId(); // Optional

        // Get the current timestamp
        String reportedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Default status is "Active"
        String status = "Active";

        // Create a new Disaster object
        Disaster disaster = new Disaster(location, disasterType, severity, status, comments, reportedTime);

        // Save the disaster report to the database using DAO
        try {
            disasterDAO.saveDisaster(disaster);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Disaster report submitted successfully!");
            alert.showAndWait();

            // Show success message and clear fields
            successLabel.setText("Success: Disaster report submitted.");
            successLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while saving the disaster report to the database.");
            alert.showAndWait();
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
