package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewDisastersController implements Initializable {

    @FXML
    private VBox disastersVBox; // VBox to dynamically add disaster information

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/disaster_reports.csv";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDisasterReports();
    }

    private void loadDisasterReports() {
        List<VBox> disasterList = new ArrayList<>(); // List to store disaster boxes

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) { // Skip header line
                    isHeader = false;
                    continue;
                }

                String[] disasterDetails = line.split(",");
                if (disasterDetails.length >= 8) {  // Assuming 8th column is the status
                    String location = disasterDetails[3];
                    String disasterType = disasterDetails[2];
                    String severity = disasterDetails[4];
                    String comments = disasterDetails[5];
                    String reportedTime = disasterDetails[6];
                    String status = disasterDetails[7]; // New status column

                    // Create a container for each disaster report
                    VBox disasterBox = new VBox(5); // spacing between elements

                    // Location Label (Bold, Font Size 14)
                    Label locationLabel = new Label(location);
                    locationLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

                    // Disaster Type Label (Bold, Italic, Font Size 12)
                    Label typeLabel = new Label("Disaster Type: " + disasterType);
                    typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 12));

                    // Reported Time Label (Bold, Font Size 12)
                    Label timeLabel = new Label("Disaster Reported: " + reportedTime);
                    timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

                    // Severity Label with Background Color
                    Label severityLabel = new Label(severity);
                    severityLabel.setFont(Font.font("Arial", 12));
                    severityLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;"); // Rounded corners and padding
                    setSeverityBackgroundColor(severityLabel, severity);

                    // Status Label with Background Color
                    Label statusLabel = new Label("Status: " + status);
                    statusLabel.setFont(Font.font("Arial", 12));
                    statusLabel.setStyle("-fx-background-radius: 5; -fx-padding: 2;"); // Rounded corners and padding
                    setStatusBackgroundColor(statusLabel, status);  // Set the background color based on status

                    // Comments Label (Description)
                    Label commentsLabel = new Label("Description: " + comments);
                    commentsLabel.setFont(Font.font("Arial", 12));

                    // Add labels to disasterBox
                    disasterBox.getChildren().addAll(locationLabel, typeLabel, timeLabel, severityLabel, statusLabel, commentsLabel);
                    disasterBox.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-background-radius: 5;");

                    // Add disasterBox to the list (instead of directly to the VBox)
                    disasterList.add(disasterBox);
                }
            }

            // Now add disasters to the VBox in reverse order (newest at the top)
            for (int i = disasterList.size() - 1; i >= 0; i--) {
                disastersVBox.getChildren().add(disasterList.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Error");
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
                label.setStyle(label.getStyle() + "-fx-background-color: orange; -fx-text-fill: black;");
                break;
            case "medium":
                label.setStyle(label.getStyle() + "-fx-background-color: yellow; -fx-text-fill: white;");
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
                label.setStyle(label.getStyle() + "-fx-background-color: none; -fx-text-fill: black;"); // Default color
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
