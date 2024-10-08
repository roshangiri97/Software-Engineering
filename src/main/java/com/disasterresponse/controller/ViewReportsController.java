package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewReportsController {

    @FXML
    private VBox reportsVBox; // VBox to hold the list of reports

    private static final String INCIDENT_REPORT_CSV_FILE = "src/main/resources/com/csv/incident_report.csv";

    @FXML
    public void initialize() {
        loadIncidentReportsData();
    }

    private void loadIncidentReportsData() {
        reportsVBox.getChildren().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(INCIDENT_REPORT_CSV_FILE))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue; // Skip the header line
                }

                String[] fields = line.split(",");
                if (fields.length >= 12) {
                    String reportId = fields[0];
                    String location = fields[1];
                    String type = fields[2];
                    String evacuations = fields[3];
                    String rescued = fields[4];
                    String casualties = fields[5];
                    String propertyDamage = fields[6];
                    String infrastructureImpact = fields[7];
                    String reliefActions = fields[8];
                    String teamsInvolved = fields[9];
                    String witnessStatement = fields[10];
                    String reportDate = fields[11];

                    VBox reportBox = new VBox(5);
                    reportBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 15; -fx-background-radius: 10;");

                    // Title label for report ID, bold and bigger font
                    Label reportIdLabel = new Label("Report ID: " + reportId);
                    reportIdLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    // Add the details with regular font and appropriate spacing
                    Label locationLabel = new Label("Location: " + location);
                    Label typeLabel = new Label("Type: " + type);
                    Label evacuationsLabel = new Label("Evacuations: " + evacuations);
                    Label rescuedLabel = new Label("Rescued: " + rescued);
                    Label casualtiesLabel = new Label("Casualties: " + casualties);
                    Label propertyDamageLabel = new Label("Property Damage: " + propertyDamage);
                    Label infrastructureImpactLabel = new Label("Infrastructure Impact: " + infrastructureImpact);
                    Label reliefActionsLabel = new Label("Relief Actions: " + reliefActions);
                    Label teamsInvolvedLabel = new Label("Teams Involved: " + teamsInvolved);
                    Label witnessStatementLabel = new Label("Witness Statement: " + witnessStatement);
                    Label reportDateLabel = new Label("Report Date: " + reportDate);

                    // Add some extra padding between sections
                    locationLabel.setStyle("-fx-padding: 5 0 5 0;");
                    typeLabel.setStyle("-fx-padding: 5 0 5 0;");
                    evacuationsLabel.setStyle("-fx-padding: 5 0 5 0;");
                    rescuedLabel.setStyle("-fx-padding: 5 0 5 0;");
                    casualtiesLabel.setStyle("-fx-padding: 5 0 5 0;");
                    propertyDamageLabel.setStyle("-fx-padding: 5 0 5 0;");
                    infrastructureImpactLabel.setStyle("-fx-padding: 5 0 5 0;");
                    reliefActionsLabel.setStyle("-fx-padding: 5 0 5 0;");
                    teamsInvolvedLabel.setStyle("-fx-padding: 5 0 5 0;");
                    witnessStatementLabel.setStyle("-fx-padding: 5 0 5 0;");
                    reportDateLabel.setStyle("-fx-padding: 5 0 5 0;");

                    // Add all labels to the VBox
                    reportBox.getChildren().addAll(reportIdLabel, locationLabel, typeLabel, evacuationsLabel, rescuedLabel, casualtiesLabel, propertyDamageLabel, infrastructureImpactLabel, reliefActionsLabel, teamsInvolvedLabel, witnessStatementLabel, reportDateLabel);

                    // Add the report box to the VBox that contains all reports
                    reportsVBox.getChildren().add(reportBox);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
