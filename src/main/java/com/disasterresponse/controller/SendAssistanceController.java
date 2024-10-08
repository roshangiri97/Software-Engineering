package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import com.disasterresponse.model.Disaster;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SendAssistanceController {

    @FXML
    private Label locationLabel;
    @FXML
    private Label disasterTypeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private CheckBox fireDeptCheckBox;
    @FXML
    private CheckBox emsCheckBox;
    @FXML
    private CheckBox policeCheckBox;
    @FXML
    private CheckBox envAgencyCheckBox;
    @FXML
    private CheckBox publicWorksCheckBox;
    @FXML
    private CheckBox utilitiesCheckBox;
    @FXML
    private CheckBox meteorologyCheckBox;
    @FXML
    private CheckBox evacuationCheckBox;
    @FXML
    private CheckBox hospitalsCheckBox;
    @FXML
    private CheckBox publicHealthCheckBox;
    @FXML
    private CheckBox pharmaceuticalCheckBox;
    @FXML
    private TextArea additionalInstructionsTextArea;

    private Disaster disaster;

    public void initializeForm(Disaster disaster) {
        this.disaster = disaster;
        locationLabel.setText(disaster.getLocation());
        disasterTypeLabel.setText(disaster.getType());
        statusLabel.setText(disaster.getStatus());
        descriptionLabel.setText(disaster.getComment());

        showDepartments(disaster.getType());
    }

    private void showDepartments(String disasterType) {
        fireDeptCheckBox.setVisible(false);
        emsCheckBox.setVisible(false);
        policeCheckBox.setVisible(false);
        envAgencyCheckBox.setVisible(false);
        publicWorksCheckBox.setVisible(false);
        utilitiesCheckBox.setVisible(false);
        meteorologyCheckBox.setVisible(false);
        evacuationCheckBox.setVisible(false);
        hospitalsCheckBox.setVisible(false);
        publicHealthCheckBox.setVisible(false);
        pharmaceuticalCheckBox.setVisible(false);

        switch (disasterType.toLowerCase()) {
            case "fire":
                fireDeptCheckBox.setVisible(true);
                emsCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                envAgencyCheckBox.setVisible(true);
                break;
            case "earthquake":
                emsCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                publicWorksCheckBox.setVisible(true);
                utilitiesCheckBox.setVisible(true);
                break;
            case "hurricane":
                meteorologyCheckBox.setVisible(true);
                evacuationCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                utilitiesCheckBox.setVisible(true);
                emsCheckBox.setVisible(true);
                break;
            case "flood":
                emsCheckBox.setVisible(true);
                publicWorksCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                break;
            case "medical":
                emsCheckBox.setVisible(true);
                hospitalsCheckBox.setVisible(true);
                publicHealthCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                pharmaceuticalCheckBox.setVisible(true);
                break;
        }
    }

    @FXML
    protected void handleSendAssistanceAction() {
        List<String> departments = new ArrayList<>();
        if (fireDeptCheckBox.isVisible() && fireDeptCheckBox.isSelected()) {
            departments.add("Fire Department");
        }
        if (emsCheckBox.isVisible() && emsCheckBox.isSelected()) {
            departments.add("EMS");
        }
        if (policeCheckBox.isVisible() && policeCheckBox.isSelected()) {
            departments.add("Police");
        }
        if (envAgencyCheckBox.isVisible() && envAgencyCheckBox.isSelected()) {
            departments.add("Environmental Agencies");
        }

        String additionalInstructions = additionalInstructionsTextArea.getText();
        String departmentList = String.join(";", departments);

        saveRescueRequest(disaster.getLocation(), disaster.getType(), departmentList, additionalInstructions);

        Stage stage = (Stage) additionalInstructionsTextArea.getScene().getWindow();
        stage.close();
    }

    private void saveRescueRequest(String location, String disasterType, String departments, String additionalInstructions) {
        String query = "INSERT INTO rescue_requests (location, disasterType, status, departments, additionalInstructions) " +
                       "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, location);
            stmt.setString(2, disasterType);
            stmt.setString(3, "Active");
            stmt.setString(4, departments);
            stmt.setString(5, additionalInstructions);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
