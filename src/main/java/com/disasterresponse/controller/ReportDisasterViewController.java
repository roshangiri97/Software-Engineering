package com.disasterresponse.controller;

import com.disasterresponse.model.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.net.URL;
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

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/disaster_reports.csv";

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

    private void saveDisasterReport(String DisasterType, String Location, String Severity, String Comments) {
        File file = new File(CSV_FILE_PATH);

        if (!file.exists()) {
            System.out.println("CSV file does not exist at: " + file.getAbsolutePath());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Generate a unique disaster ID
            int disasterId = generateDisasterId();

            // Get the currently logged-in user's ID from SessionManager
            String userId = SessionManager.getInstance().getUserId();

            // Get the current timestamp
            String reportedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Default status is "Active"
            String status = "Active";

            // Write the disaster report to the CSV file in the correct column order
            writer.write(disasterId + "," + userId + "," + DisasterType + "," + Location + "," + Severity + "," + Comments + "," + reportedTime + "," + status);
            writer.newLine();
            writer.flush(); // Ensure the data is written to the file

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Disaster report submitted successfully!");
            alert.showAndWait();

            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Error");
            alert.setContentText("An error occurred while saving the disaster report.");
            alert.showAndWait();
        }
    }

    private int generateDisasterId() {
        int maxDisasterId = 0; // Start with 0 and increment based on existing disaster IDs
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true; // Skip header line
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] disasterDetails = line.split(",");
                if (disasterDetails.length > 0) {
                    try {
                        int currentDisasterId = Integer.parseInt(disasterDetails[0].trim());
                        if (currentDisasterId > maxDisasterId) {
                            maxDisasterId = currentDisasterId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid disasterId format: " + disasterDetails[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxDisasterId + 1; // Increment the highest found disasterId by 1 for the new report
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
