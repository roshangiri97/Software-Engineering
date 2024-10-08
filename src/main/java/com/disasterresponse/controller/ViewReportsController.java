package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import dao.DisasterDAO;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ViewReportsController {

    @FXML
    private VBox reportsVBox; // VBox to hold the list of reports

    private DisasterDAO disasterDAO = new DisasterDAO(); // DAO to interact with the database

    @FXML
    public void initialize() {
        loadIncidentReportsData(); // Load data from the database
    }

    private void loadIncidentReportsData() {
        reportsVBox.getChildren().clear(); // Clear any previous entries

        try {
            // Fetch all disasters from the database using the DAO
            List<Disaster> disasterList = disasterDAO.getAllDisasters();

            // Loop through each disaster and display it in the UI
            for (Disaster disaster : disasterList) {
                VBox reportBox = new VBox(5);
                reportBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 15; -fx-background-radius: 10;");

                // Title label for report ID, bold and bigger font
                Label locationLabel = new Label("Location: " + disaster.getLocation());
                locationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                // Add the details with regular font and appropriate spacing
                Label typeLabel = new Label("Disaster Type: " + disaster.getType());
                Label severityLabel = new Label("Severity: " + disaster.getSeverity());
                Label statusLabel = new Label("Status: " + disaster.getStatus());
                Label commentLabel = new Label("Comments: " + disaster.getComment());
                Label reportedTimeLabel = new Label("Reported Time: " + disaster.getReportedTime());

                // Add all labels to the VBox
                reportBox.getChildren().addAll(locationLabel, typeLabel, severityLabel, statusLabel, commentLabel, reportedTimeLabel);

                // Add the report box to the VBox that contains all reports
                reportsVBox.getChildren().add(reportBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "Failed to load disaster reports from the database.");
        }
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    protected void handleBackAction() {
        // Return to the homepage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Re-initialize the homepage
            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) reportsVBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
