package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import com.disasterresponse.model.SessionManager;
import dao.DisasterDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReportDisasterViewController implements Initializable {

    @FXML
    private ComboBox<String> disasterTypeComboBox;

    @FXML
    private TextField locationField;

    @FXML
    private ComboBox<String> severityComboBox;

    @FXML
    private TextArea commentsArea;

    private DisasterDAO disasterDAO = new DisasterDAO(); // DAO to handle database operations

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize ComboBox values for disaster types and severity levels
        disasterTypeComboBox.getItems().addAll("Hurricane", "Fire", "Earthquake", "Flood", "Medical", "Other");
        severityComboBox.getItems().addAll("Low", "Medium", "High", "Critical");
    }

    @FXML
    protected void handleSubmitAction() {
        String disasterType = disasterTypeComboBox.getValue();
        String location = locationField.getText().trim();
        String severity = severityComboBox.getValue();
        String comments = commentsArea.getText().trim();

        if (disasterType == null || location.isEmpty() || severity == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Missing Information");
            alert.setContentText("Please fill in all the required fields.");
            alert.showAndWait();
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
        disasterTypeComboBox.setValue(null);
        locationField.clear();
        severityComboBox.setValue(null);
        commentsArea.clear();
    }

    @FXML
    protected void handleCancelAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) disasterTypeComboBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
