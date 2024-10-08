package com.disasterresponse.controller;


import com.disasterresponse.DatabaseConnection;

import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import dao.DisasterDAO;
import com.disasterresponse.model.Disaster;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class HomepageController {

    @FXML
    private Button manageUsersButton;
    @FXML
    private Button generateReportsButton;
    @FXML
    private Button assistanceButton;
    @FXML
    private Button viewDisastersButton; // Button to view disasters
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox recentAlertsVBox; // VBox to display recent alerts
    @FXML
    private Button rescueRequestsButton; // Button for Rescue Requests

    private String userRole;
    private String username;

    private DisasterDAO disasterDAO = new DisasterDAO(); // DAO for database access

    public void initializePage() {
        // Retrieve user session details
        this.username = SessionManager.getInstance().getUsername();
        this.userRole = SessionManager.getInstance().getUserAccessLevel();

        // Set welcome message with username
        welcomeLabel.setText("Welcome " + SessionManager.getInstance().getFullName());

        // Update UI elements based on user role
        updateUIBasedOnRole();

        // Load recent disaster alerts from the database
        loadRecentAlerts();
    }

    // Hides or shows buttons based on user roles
    private void updateUIBasedOnRole() {
        if ("Admin".equalsIgnoreCase(userRole)) {
            manageUsersButton.setVisible(true);
            generateReportsButton.setVisible(true);
            assistanceButton.setVisible(true);
            rescueRequestsButton.setVisible(true);
        } else if ("Agencies or Organization".equalsIgnoreCase(userRole)) {
            manageUsersButton.setVisible(false);
            generateReportsButton.setVisible(true);
            assistanceButton.setVisible(true);
            rescueRequestsButton.setVisible(false);
        } else if ("Emergency Responders".equalsIgnoreCase(userRole)) {
            manageUsersButton.setVisible(false);
            generateReportsButton.setVisible(false);
            assistanceButton.setVisible(false);
            rescueRequestsButton.setVisible(true);
        } else {
            // General Public or other roles
            manageUsersButton.setVisible(false);
            generateReportsButton.setVisible(false);
            assistanceButton.setVisible(false);
            rescueRequestsButton.setVisible(false);
        }
    }

    // Loads recent alerts from the database
    private void loadRecentAlerts() {
        recentAlertsVBox.getChildren().clear();
        String query = "SELECT * FROM disaster_reports ORDER BY reportedTime DESC LIMIT 3";   

        try {
            // Get the latest 3 disasters from the database
            List<Disaster> recentDisasters = disasterDAO.getLatestDisasters(3);

            // Display the latest disasters
            for (Disaster disaster : recentDisasters) {
                VBox disasterBox = new VBox(5);
                disasterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");

                // Location Label (Header)
                Label locationLabel = new Label(disaster.getLocation());
                locationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                // Disaster Type Label (Bold)
                Label typeLabel = new Label("Disaster Type: " + disaster.getType());
                typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

                // Severity Label with color based on severity
                Label severityLabel = new Label(disaster.getSeverity());
                severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;");
                setSeverityBackgroundColor(severityLabel, disaster.getSeverity());

                // Time Label
                Label timeLabel = new Label("Reported: " + disaster.getReportedTime());
                timeLabel.setStyle("-fx-font-style: italic;");

                disasterBox.getChildren().addAll(locationLabel, typeLabel, severityLabel, timeLabel);
                recentAlertsVBox.getChildren().add(disasterBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sets background color based on severity of the disaster
    private void setSeverityBackgroundColor(Label label, String severity) {
        switch (severity.toLowerCase()) {
            case "critical":
                label.setStyle(label.getStyle() + "-fx-background-color: red; -fx-text-fill: white;");
                break;
            case "high":
                label.setStyle(label.getStyle() + "-fx-background-color: orange; -fx-text-fill: white;");
                break;
            case "medium":
                label.setStyle(label.getStyle() + "-fx-background-color: yellow; -fx-text-fill: black;");
                break;
            case "low":
                label.setStyle(label.getStyle() + "-fx-background-color: lightgreen; -fx-text-fill: black;");
                break;
            default:
                label.setStyle(label.getStyle() + "-fx-background-color: none; -fx-text-fill: black;");
                break;
        }
    }

    // Handlers for the buttons and actions

    @FXML
    protected void handleReportDisasterAction() {
        loadView("/com/disasterresponse/view/ReportDisasterView.fxml");
    }

    @FXML
    protected void handleGenerateReportsAction() {
        loadView("/com/disasterresponse/view/ViewReportsView.fxml");
    }

    @FXML
    protected void handleManageUsersAction() {
        loadView("/com/disasterresponse/view/ManageUsersView.fxml");
    }

    @FXML
    protected void handleImmediateAssistanceAction() {
        loadView("/com/disasterresponse/view/ManageDisasterView.fxml");
    }

    @FXML
    protected void handleViewDisastersAction() {
        loadView("/com/disasterresponse/view/ViewDisastersView.fxml");
    }

    @FXML
    protected void handleRescueRequestsAction() {
        loadView("/com/disasterresponse/view/RescueRequestsView.fxml");
    }

    @FXML
    protected void handleLogoutAction() {
        SessionManager.getInstance().clearSession();
        loadView("/com/disasterresponse/view/LoginView.fxml");
    }

    // Utility method to load a new view (FXML)
    private void loadView(String viewPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading view: " + viewPath + ", Error: " + e.getMessage());
        }
    }
}
