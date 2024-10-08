package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewDisastersController implements Initializable {

    @FXML
    private VBox disastersVBox; // VBox to dynamically add disaster information

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDisasterReports();
    }

    private void loadDisasterReports() {
        List<VBox> disasterList = new ArrayList<>(); // List to store disaster boxes

        String query = "SELECT Location, DisasterType, Severity, Comments, reportedTime, status FROM disaster_reports ORDER BY reportedTime DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String location = rs.getString("Location");
                String disasterType = rs.getString("DisasterType");
                String severity = rs.getString("Severity");
                String comments = rs.getString("Comments");
                String reportedTime = rs.getString("reportedTime");
                String status = rs.getString("status");

                // Create a container for each disaster report
                VBox disasterBox = new VBox(5); // spacing between elements
                disasterBox.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: lightgrey; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 0, 2);");

                // Location Label (Bold, Font Size 14)
                Label locationLabel = new Label(location);
                locationLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
                locationLabel.setStyle("-fx-text-fill: #333333;");

                // Disaster Type Label (Bold, Italic, Font Size 12)
                Label typeLabel = new Label("Disaster Type: " + disasterType);
                typeLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, javafx.scene.text.FontPosture.ITALIC, 12));

                // Reported Time Label (Bold, Font Size 12)
                Label timeLabel = new Label("Disaster Reported: " + reportedTime);
                timeLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.NORMAL, 12));
                timeLabel.setStyle("-fx-text-fill: #666666;");

                // Severity Label with Background Color
                Label severityLabel = new Label(severity);
                severityLabel.setFont(javafx.scene.text.Font.font("Arial", 12));
                severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 5; -fx-font-weight: bold;"); // Rounded corners and padding
                setSeverityBackgroundColor(severityLabel, severity);

                // Status Label with Background Color
                Label statusLabel = new Label("Status: " + status);
                statusLabel.setFont(javafx.scene.text.Font.font("Arial", 12));
                statusLabel.setStyle("-fx-background-radius: 5; -fx-padding: 5; -fx-font-weight: bold;"); // Rounded corners and padding
                setStatusBackgroundColor(statusLabel, status);  // Set the background color based on status

                // Comments Label (Description)
                Label commentsLabel = new Label("Description: " + comments);
                commentsLabel.setFont(javafx.scene.text.Font.font("Arial", 12));
                commentsLabel.setStyle("-fx-text-fill: #666666;");

                // Add labels to disasterBox
                disasterBox.getChildren().addAll(locationLabel, typeLabel, timeLabel, severityLabel, statusLabel, commentsLabel);

                // Add disasterBox to the list
                disasterList.add(disasterBox);
            }

            // Now add disasters to the VBox in reverse order (newest at the top)
            for (int i = disasterList.size() - 1; i >= 0; i--) {
                disastersVBox.getChildren().add(disasterList.get(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while loading disaster reports.");
            alert.showAndWait();
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
                label.setStyle(label.getStyle() + "-fx-background-color: green; -fx-text-fill: black;");
                break;
            default:
                label.setStyle(label.getStyle() + "-fx-background-color: none; -fx-text-fill: black;");
                break;
        }
    }

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

    @FXML
    protected void handleBackToHomeAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Re-initialize the homepage if returning
            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) disastersVBox.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
