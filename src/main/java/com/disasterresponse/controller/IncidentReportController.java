package com.disasterresponse.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class IncidentReportController {

    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField evacuationsField;
    @FXML
    private TextField rescuedField;
    @FXML
    private TextField casualtiesField;
    @FXML
    private TextField propertyDamageField;
    @FXML
    private TextField infrastructureImpactField;
    @FXML
    private TextField reliefActionsField;
    @FXML
    private TextField teamsInvolvedField;
    @FXML
    private TextArea witnessStatementField;
    @FXML
    private TextField reportDateField;

    private String requestId;  // To store the request ID passed from RescueRequestsController
    private static final String RESCUE_REQUESTS_CSV_FILE = "src/main/resources/com/csv/rescue_requests.csv";
    private static final String INCIDENT_REPORT_CSV_FILE = "src/main/resources/com/csv/incident_report.csv";

    public void initializeForm(String requestId) {
        this.requestId = requestId;
        loadRescueRequestData(requestId);

        // Prefill the report date with the current date
        reportDateField.setText(LocalDate.now().toString());
    }

    private void loadRescueRequestData(String requestId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESCUE_REQUESTS_CSV_FILE))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;  // Skip the header line
                }

                String[] fields = line.split(",");
                if (fields.length >= 6 && fields[0].equals(requestId)) {
                    locationField.setText(fields[1]);  // Prefill location
                    typeField.setText(fields[2]);      // Prefill disaster type
                    teamsInvolvedField.setText(fields[4]);  // Prefill teams involved
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleSubmitReportAction() {
        String location = locationField.getText();
        String type = typeField.getText();
        String evacuations = evacuationsField.getText();
        String rescued = rescuedField.getText();
        String casualties = casualtiesField.getText();
        String propertyDamage = propertyDamageField.getText();
        String infrastructureImpact = infrastructureImpactField.getText();
        String reliefActions = reliefActionsField.getText();
        String teamsInvolved = teamsInvolvedField.getText();
        String witnessStatement = witnessStatementField.getText();
        String reportDate = reportDateField.getText();

        // Save the incident report to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INCIDENT_REPORT_CSV_FILE, true))) {
            writer.write(requestId + "," + location + "," + type + "," + evacuations + "," + rescued + "," + casualties + ","
                    + propertyDamage + "," + infrastructureImpact + "," + reliefActions + "," + teamsInvolved + ","
                    + witnessStatement + "," + reportDate);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the form after submitting the report
        Stage stage = (Stage) reportDateField.getScene().getWindow();
        stage.close();
    }
}
