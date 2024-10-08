package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import com.disasterresponse.model.SessionManager;
import dao.DisasterDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class ViewDisastersController implements Initializable {

    @FXML
    private VBox disastersVBox; // VBox to dynamically add disaster reports

    private DisasterDAO disasterDAO = new DisasterDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDisasterReports();
    }

    // Method to load disaster reports from the database
    private void loadDisasterReports() {
        try {
            List<Disaster> disasterList = disasterDAO.getAllDisasters();

            for (Disaster disaster : disasterList) {
                VBox disasterBox = new VBox(5); // Spacing between elements

                // Create labels for each field
                Label locationLabel = new Label(disaster.getLocation());
                locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                Label typeLabel = new Label("Disaster Type: " + disaster.getType());
                typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 12));

                Label timeLabel = new Label("Disaster Reported: " + disaster.getReportedTime());
                timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                Label severityLabel = new Label(disaster.getSeverity());
                severityLabel.setFont(Font.font("Arial", 12));
                severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;");
                setSeverityBackgroundColor(severityLabel, disaster.getSeverity());

                Label statusLabel = new Label("Status: " + disaster.getStatus());
                statusLabel.setFont(Font.font("Arial", 12));
                statusLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;");
                setStatusBackgroundColor(statusLabel, disaster.getStatus());

                Label commentsLabel = new Label("Description: " + disaster.getComment());
                commentsLabel.setFont(Font.font("Arial", 12));

                // Add labels to the VBox for the individual disaster
                disasterBox.getChildren().addAll(locationLabel, typeLabel, timeLabel, severityLabel, statusLabel, commentsLabel);
                disasterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");

                // Add each disasterBox to the disastersVBox
                disastersVBox.getChildren().add(disasterBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error Loading Disaster Reports");
            alert.setContentText("An error occurred while fetching disaster reports from the database.");
            alert.showAndWait();
        }
    }

    // Back to Home button handler
    @FXML
    private void handleBackToHomeAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Re-initialize the homepage with the session data when returning
            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) disastersVBox.getScene().getWindow(); // Get the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to set the background color based on severity
    private void setSeverityBackgroundColor(Label label, String severity) {
        switch (severity.toLowerCase()) {
            case "critical":
                label.setStyle(label.getStyle() + "-fx-background-color: red; -fx-text-fill: white;");
                break;
            case "high":
                label.setStyle(label.getStyle() + "-fx-background-color: orange; -fx-text-fill: black;");
                break;
            case "medium":
                label.setStyle(label.getStyle() + "-fx-background-color: yellow; -fx-text-fill: black;");
                break;
            case "low":
                label.setStyle(label.getStyle() + "-fx-background-color: green; -fx-text-fill: black;");
                break;
            default:
                label.setStyle(label.getStyle() + "-fx-background-color: none; -fx-text-fill: black;");
                break;
        }
    }

    // Method to set the background color based on the status
    private void setStatusBackgroundColor(Label label, String status) {
        switch (status.toLowerCase()) {
            case "active":
                label.setStyle(label.getStyle() + "-fx-background-color: green; -fx-text-fill: white;");
                break;
            case "help on board":
                label.setStyle(label.getStyle() + "-fx-background-color: blue; -fx-text-fill: white;");
                break;
            case "under control":
                label.setStyle(label.getStyle() + "-fx-background-color: grey; -fx-text-fill: white;");
                break;
            case "inactive":
                label.setStyle(label.getStyle() + "-fx-background-color: white; -fx-text-fill: black;");
                break;
            default:
                label.setStyle(label.getStyle() + "-fx-background-color: none; -fx-text-fill: black;");
                break;
        }
    }
}
