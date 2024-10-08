package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class HomepageController {

    @FXML
    private Button manageUsersButton;
    @FXML
    private Button generateReportsButton;
    @FXML
    private Button assistanceButton;
    @FXML
    private Button viewDisastersButton;
    @FXML
    private Button rescueRequestsButton;
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox recentAlertsVBox;

    private String userRole;
    private String username;

    // Initializes the page and sets up the interface
    public void initializePage() {
        this.username = SessionManager.getInstance().getUsername();
        this.userRole = SessionManager.getInstance().getUserRole();

        welcomeLabel.setText("Welcome, " + this.username);
        updateUIBasedOnRole();
        loadRecentAlerts();
    }

    // Hides or shows buttons based on user roles
    private void updateUIBasedOnRole() {
        // Hide all buttons first, then enable based on role
        manageUsersButton.setVisible(false);
        generateReportsButton.setVisible(false);
        assistanceButton.setVisible(false);
        viewDisastersButton.setVisible(true);
        rescueRequestsButton.setVisible(false);

        // Adjust visibility based on the user's role
        switch (userRole.toLowerCase()) {
            case "admin":
                manageUsersButton.setVisible(true);
                generateReportsButton.setVisible(true);
                assistanceButton.setVisible(true);
                viewDisastersButton.setVisible(true);
                rescueRequestsButton.setVisible(true);
                break;
            case "agencies or organization":
                generateReportsButton.setVisible(true);
                assistanceButton.setVisible(true);
                rescueRequestsButton.setVisible(true);
                break;
            case "emergency responders":
                viewDisastersButton.setVisible(true);
                rescueRequestsButton.setVisible(true);
                break;
            default:
                // Public or undefined roles, no buttons enabled
                break;
        }
    }

    // Loads recent alerts from the database
    private void loadRecentAlerts() {
        recentAlertsVBox.getChildren().clear();
        String query = "SELECT * FROM disaster_reports ORDER BY reportedTime DESC LIMIT 3";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            List<String[]> recentDisasters = new ArrayList<>();
            while (rs.next()) {
                String[] disasterDetails = {
                    rs.getString("Location"),
                    rs.getString("DisasterType"),
                    rs.getString("Severity"),
                    rs.getString("reportedTime")
                };
                recentDisasters.add(disasterDetails);
            }

            for (String[] disasterDetails : recentDisasters) {
                String location = disasterDetails[0];
                String disasterType = disasterDetails[1];
                String severity = disasterDetails[2];
                String reportedTime = disasterDetails[3];

                VBox disasterBox = new VBox(5);
                disasterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");

                Label locationLabel = new Label(location);
                locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Label typeLabel = new Label("Disaster Type: " + disasterType);
                typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                Label severityLabel = new Label(severity);
                severityLabel.setFont(Font.font("Arial", 12));
                severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;");
                setSeverityBackgroundColor(severityLabel, severity);

                Label timeLabel = new Label(reportedTime);
                timeLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 12));

                disasterBox.getChildren().addAll(locationLabel, typeLabel, severityLabel, timeLabel);
                recentAlertsVBox.getChildren().add(disasterBox);
            }

        } catch (SQLException e) {
            System.err.println("Error loading recent alerts: " + e.getMessage());
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
