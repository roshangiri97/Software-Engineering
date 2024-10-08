package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RescueRequestsController {

    @FXML
    private VBox requestsVBox;

    private static final String RESCUE_REQUESTS_CSV_FILE = "src/main/resources/com/csv/rescue_requests.csv";

    @FXML
    public void initialize() {
        loadRescueRequestsData();
    }

    private void loadRescueRequestsData() {
        requestsVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(RESCUE_REQUESTS_CSV_FILE))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header line
                }

                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String requestId = fields[0];
                    String location = fields[1];
                    String disasterType = fields[2];
                    String status = fields[3];
                    String departments = fields[4];
                    String additionalInstructions = fields[5];

                    VBox requestBox = new VBox(10);
                    requestBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 10;");

                    // Request ID Label
                    Label requestIdLabel = new Label("Request ID: " + requestId);
                    requestIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    // Location Label
                    Label locationLabel = new Label("Location: " + location);

                    // Disaster Type Label
                    Label disasterTypeLabel = new Label("Disaster Type: " + disasterType);

                    // Status Label
                    Label statusLabel = new Label("Status: " + status);
                    statusLabel.setStyle("-fx-background-color: " + getStatusColor(status));

                    // Departments Label
                    Label departmentsLabel = new Label("Departments: " + departments);

                    // Additional Instructions Label
                    Label instructionsLabel = new Label("Additional Instructions: " + additionalInstructions);

                    // Add a "Incident Report" button
                    Button incidentReportButton = new Button("Incident Report");
                    incidentReportButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
                    incidentReportButton.setOnAction(e -> handleIncidentReportAction(requestId));

                    requestBox.getChildren().addAll(requestIdLabel, locationLabel, disasterTypeLabel, statusLabel, departmentsLabel, instructionsLabel, incidentReportButton);
                    requestsVBox.getChildren().add(requestBox);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle "Incident Report" button click
    private void handleIncidentReportAction(String requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/IncidentReportForm.fxml"));
            Parent root = loader.load();

            // Pass the requestId to the IncidentReportController
            IncidentReportController controller = loader.getController();
            controller.initializeForm(requestId);

            Stage stage = new Stage();
            stage.setTitle("Submit Incident Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to determine the color of the status label
    private String getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "active":
                return "green";
            case "inactive":
                return "gray";
            case "help on board":
                return "blue";
            case "under control":
                return "orange";
            default:
                return "black";
        }
    }

    @FXML
    protected void handleBackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Re-initialize the homepage
            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) requestsVBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
