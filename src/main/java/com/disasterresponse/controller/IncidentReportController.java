package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private String requestId;

    // Method to initialize the form with data for the given request ID
    public void initializeForm(String requestId) {
        this.requestId = requestId;
        loadRescueRequestData(requestId);

        // Optionally, you can prefill the report date with the current date
        reportDateField.setText(java.time.LocalDate.now().toString());
    }

    private void loadRescueRequestData(String requestId) {
        String query = "SELECT * FROM rescue_requests WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                locationField.setText(rs.getString("location"));
                typeField.setText(rs.getString("disasterType"));
                teamsInvolvedField.setText(rs.getString("departments"));
            }

        } catch (SQLException e) {
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

        saveIncidentReport(requestId, location, type, evacuations, rescued, casualties, propertyDamage, infrastructureImpact, reliefActions, teamsInvolved, witnessStatement, reportDate);

        Stage stage = (Stage) reportDateField.getScene().getWindow();
        stage.close();
    }

    private void saveIncidentReport(String requestId, String location, String type, String evacuations, String rescued, String casualties, String propertyDamage, String infrastructureImpact, String reliefActions, String teamsInvolved, String witnessStatement, String reportDate) {
        String query = "INSERT INTO incident_reports (request_id, location, type, evacuations, rescued, casualties, property_damage, infrastructure_impact, relief_actions, teams_involved, witness_statement, report_date) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, requestId);
            stmt.setString(2, location);
            stmt.setString(3, type);
            stmt.setString(4, evacuations);
            stmt.setString(5, rescued);
            stmt.setString(6, casualties);
            stmt.setString(7, propertyDamage);
            stmt.setString(8, infrastructureImpact);
            stmt.setString(9, reliefActions);
            stmt.setString(10, teamsInvolved);
            stmt.setString(11, witnessStatement);
            stmt.setString(12, reportDate);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
