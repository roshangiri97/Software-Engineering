package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import com.disasterresponse.model.IncidentReport;
import dao.IncidentReportDAO;
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

    private IncidentReportDAO incidentReportDAO = new IncidentReportDAO(); // DAO to interact with the database

    @FXML
    public void initialize() {
        loadIncidentReportsData(); // Load data from the database
    }

    private void loadIncidentReportsData() {
        reportsVBox.getChildren().clear(); // Clear any previous entries
        // Fetch all disasters from the database using the DAO
        List<IncidentReport> incidentReportList = incidentReportDAO.getAllIncidentReports();
        // Loop through each disaster and display it in the UI
        for (IncidentReport incidentReport : incidentReportList) {
            VBox reportBox = new VBox(5);
            reportBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 15; -fx-background-radius: 10;");
            
            // Title label for report ID, bold and bigger font
            Label locationLabel = new Label("Location: " + incidentReport.getLocation());
            locationLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            
            // Add the details with regular font and appropriate spacing
            Label typeLabel = new Label("Incident Type: " + incidentReport.getType());
            Label evacuationsLabel = new Label("Evacuation: " + incidentReport.getEvacuations());
            Label rescuedLabel = new Label("Rescued: " + incidentReport.getRescued());
            Label casualtiesLabel = new Label("Casualties: " + incidentReport.getCasualties());
            Label commentLabel = new Label("Teams Involved: " + incidentReport.getTeamsInvolved());
            Label reportedTimeLabel = new Label("Reported Date: " + incidentReport.getReportDate());
            
            // Add all labels to the VBox
            reportBox.getChildren().addAll(locationLabel, typeLabel, evacuationsLabel, rescuedLabel,casualtiesLabel ,commentLabel, reportedTimeLabel);
            
            // Add the report box to the VBox that contains all reports
            reportsVBox.getChildren().add(reportBox);
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
