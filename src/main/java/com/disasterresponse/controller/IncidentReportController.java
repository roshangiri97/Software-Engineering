package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.IncidentReport;
import com.disasterresponse.model.SessionManager;
import dao.IncidentReportDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.scene.control.Label;

public class IncidentReportController {

    @FXML
     TextField locationField;
    @FXML
     TextField typeField;
    @FXML
     TextField evacuationsField;
    @FXML
     TextField rescuedField;
    @FXML
     TextField casualtiesField;
    @FXML
     TextField propertyDamageField;
    @FXML
     TextField infrastructureImpactField;
    @FXML
     TextField reliefActionsField;
    @FXML
     TextField teamsInvolvedField;
    @FXML
     TextArea witnessStatementField;
    @FXML
     TextField reportDateField;

     String requestId;
 @FXML
    public Label errorMessageLabel; // Add this line
 
    public IncidentReportDAO incidentReportDAO = new IncidentReportDAO();

    // Method to initialize the form with data for the given request ID
    public void initializeForm(String requestId) {
        this.requestId = requestId;
        loadRescueRequestData(requestId);

        // Optionally, you can prefill the report date with the current date
        reportDateField.setText(java.time.LocalDate.now().toString());
    }

    public void loadRescueRequestData(String requestId) {
        String query = "SELECT * FROM rescue_requests WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

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
    public void handleSubmitReportAction() {
        String location = locationField.getText();
        String type = typeField.getText();
        String evacuationsString = evacuationsField.getText();
        String rescuedString = rescuedField.getText();
        String casualtiesString = casualtiesField.getText();
        String propertyDamage = propertyDamageField.getText();
        String infrastructureImpact = infrastructureImpactField.getText();
        String reliefActions = reliefActionsField.getText();
        String teamsInvolved = teamsInvolvedField.getText();
        String witnessStatement = witnessStatementField.getText();
        String reportDate = reportDateField.getText();

        String userIdString = SessionManager.getInstance().getUserId();

        int userId = Integer.parseInt(userIdString);
        int requestId = Integer.parseInt(this.requestId);
        int evacuations = Integer.parseInt(evacuationsString);
        int rescued = Integer.parseInt(rescuedString);
        int casualties = Integer.parseInt(casualtiesString);

        IncidentReport report = new IncidentReport(requestId, location, type,
                evacuations,
                rescued,
                casualties,
                propertyDamage,
                infrastructureImpact,
                reliefActions,
                teamsInvolved,
                witnessStatement,
                convertReportDate(reportDate)
        );
        incidentReportDAO.saveIncidentReport(report);

        Stage stage = (Stage) reportDateField.getScene().getWindow();
        stage.close();
    }
    
    public Date convertReportDate(String reportDateString) {

        Date sqlDate = null;
        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // Parse the string to LocalDate
            LocalDate localDate = LocalDate.parse(reportDateString, formatter);
            // Convert LocalDate to java.sql.Date
            sqlDate = Date.valueOf(localDate);
            
            // Now you can use sqlDate in your SQL queries
            return sqlDate;
        } catch (DateTimeParseException e) {
           // e.printStackTrace(); // Handle the exception as needed
        }
        return sqlDate;
    }
}
