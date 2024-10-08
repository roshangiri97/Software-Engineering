package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RescueRequestsController {

    @FXML
    private VBox requestsVBox;

    @FXML
    public void initialize() {
        loadRescueRequestsData();
    }

    private void loadRescueRequestsData() {
        requestsVBox.getChildren().clear();

        String query = "SELECT * FROM rescue_requests";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String requestId = rs.getString("request_id");
                String location = rs.getString("location");
                String disasterType = rs.getString("disasterType");
                String status = rs.getString("status");
                String departments = rs.getString("departments");
                String additionalInstructions = rs.getString("additionalInstructions");

                VBox requestBox = new VBox(10);
                requestBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 10;");

                Label requestIdLabel = new Label("Request ID: " + requestId);
                requestIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                Label locationLabel = new Label("Location: " + location);
                Label disasterTypeLabel = new Label("Disaster Type: " + disasterType);
                Label statusLabel = new Label("Status: " + status);
                Label departmentsLabel = new Label("Departments: " + departments);
                Label instructionsLabel = new Label("Additional Instructions: " + additionalInstructions);

                Button incidentReportButton = new Button("Incident Report");
                incidentReportButton.setStyle("-fx-background-color: #000000; -fx-text-fill: white;");
                incidentReportButton.setOnAction(e -> handleIncidentReportAction(requestId));

                requestBox.getChildren().addAll(requestIdLabel, locationLabel, disasterTypeLabel, statusLabel, departmentsLabel, instructionsLabel, incidentReportButton);
                requestsVBox.getChildren().add(requestBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
                    // Handle exceptions here
        }
    }

    private void handleIncidentReportAction(String requestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/IncidentReportForm.fxml"));
            Parent root = loader.load();

            IncidentReportController controller = loader.getController();
            controller.initializeForm(requestId);

            Stage stage = new Stage();
            stage.setTitle("Submit Incident Report");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) requestsVBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

