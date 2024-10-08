package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.disasterresponse.model.SessionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private Button viewDisastersButton; // Add button for viewing disasters
    @FXML
    private Label welcomeLabel;
    @FXML
    private VBox recentAlertsVBox; // Add VBox to display recent alerts
     @FXML
    private Button rescueRequestsButton; // New Button for Rescue Requests

    private String userRole;
    private String username;

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/disaster_reports.csv";

    public void initializePage() {
        // Get user details from SessionManager
        this.username = SessionManager.getInstance().getUsername();
        this.userRole = SessionManager.getInstance().getUserRole();

        welcomeLabel.setText("Welcome " + this.username);  // Set welcome message with username
        updateUIBasedOnRole();  // Setup the homepage based on the user's role
        loadRecentAlerts();  // Load recent disaster alerts
    }

    @FXML
    protected void handleLoginSuccess() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Get the stage and apply the scene
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);

            // Make the stage resizable
            stage.setResizable(true);

            // Set initial dimensions (but the user can resize it)
            stage.setWidth(600);  // Set preferred width
            stage.setHeight(700);  // Set preferred height

            // Show the stage
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }else if ("Emergency Responders".equalsIgnoreCase(userRole)) {
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

    private void loadRecentAlerts() {
        recentAlertsVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isHeader = true;
            List<String[]> recentDisasters = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] disasterDetails = line.split(",");
                recentDisasters.add(disasterDetails);
            }

            // Display the latest 3 disasters
            int count = Math.min(recentDisasters.size(), 3);
            for (int i = recentDisasters.size() - 1; i >= recentDisasters.size() - count; i--) {
                String[] disasterDetails = recentDisasters.get(i);

                String location = disasterDetails[3];
                String disasterType = disasterDetails[2];
                String severity = disasterDetails[4];
                String reportedTime = disasterDetails[6];

                VBox disasterBox = new VBox(5);
                disasterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");

                // Location Label (Header)
                Label locationLabel = new Label(location);
                locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                // Disaster Type Label (Bold)
                Label typeLabel = new Label("Disaster Type: " + disasterType);
                typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                // Severity Label with color based on severity
                Label severityLabel = new Label(severity);
                severityLabel.setFont(Font.font("Arial", 12));
                severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;");
                setSeverityBackgroundColor(severityLabel, severity);

                // Time Label in Italic
                Label timeLabel = new Label(reportedTime);
                timeLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 12));

                // Add labels to the VBox
                disasterBox.getChildren().addAll(locationLabel, typeLabel, severityLabel, timeLabel);
                recentAlertsVBox.getChildren().add(disasterBox);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    protected void handleViewDisastersAction() { // Handle the button to view disasters
        loadView("/com/disasterresponse/view/ViewDisastersView.fxml");
    }
    @FXML
    protected void handleRescueRequestsAction() {
        loadView("/com/disasterresponse/view/RescueRequestsView.fxml");
    }

    @FXML
    protected void handleLogoutAction() {
        // Clear session and go back to login
        SessionManager.getInstance().clearSession();
        loadView("/com/disasterresponse/view/LoginView.fxml");
    }

    private void loadView(String viewPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent root = loader.load();

            // Re-initialize the homepage if returning
            if (viewPath.equals("/com/disasterresponse/view/HomepageView.fxml")) {
                HomepageController controller = loader.getController();
                controller.initializePage();  // Make sure to reinitialize the homepage
            }

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading view: " + viewPath);
        }
    }
}
