package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private static final String RESCUE_REQUESTS_CSV_FILE = "src/main/resources/com/csv/rescue_requests.csv";

    public void initializeForm(Disaster disaster) {
        this.disaster = disaster;
        locationLabel.setText(disaster.getLocation());
        disasterTypeLabel.setText(disaster.getType());
        statusLabel.setText(disaster.getStatus());
        descriptionLabel.setText(disaster.getComment());  // Show the comment field for description

        // Show departments based on disaster type
        showDepartments(disaster.getType());
    }

    // Show departments based on the disaster type
    private void showDepartments(String disasterType) {
        System.out.println("Disaster Type: " + disasterType); // Debug statement

        // First, hide all checkboxes
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
            case "medical":  // Adjusted to match the type "Medical"
            case "medical emergency": // In case other entries come with the full term
                System.out.println("Medical Emergency Departments should be visible"); // Debug statement
                emsCheckBox.setVisible(true);
                hospitalsCheckBox.setVisible(true);
                publicHealthCheckBox.setVisible(true);
                policeCheckBox.setVisible(true);
                pharmaceuticalCheckBox.setVisible(true);
                break;
            default:
                System.out.println("Unknown disaster type: " + disasterType); // Debug statement
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
        if (publicWorksCheckBox.isVisible() && publicWorksCheckBox.isSelected()) {
            departments.add("Public Works");
        }
        if (utilitiesCheckBox.isVisible() && utilitiesCheckBox.isSelected()) {
            departments.add("Utilities");
        }
        if (meteorologyCheckBox.isVisible() && meteorologyCheckBox.isSelected()) {
            departments.add("Meteorology");
        }
        if (evacuationCheckBox.isVisible() && evacuationCheckBox.isSelected()) {
            departments.add("Evacuation Authorities");
        }

        String additionalInstructions = additionalInstructionsTextArea.getText();

        int requestId = generateRequestId();

        // Save the rescue request to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESCUE_REQUESTS_CSV_FILE, true))) {
            writer.write(requestId + "," + disaster.getLocation() + "," + disaster.getType() + "," + disaster.getStatus() + ","
                    + String.join(";", departments) + "," + additionalInstructions);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Close the form after sending assistance
        Stage stage = (Stage) additionalInstructionsTextArea.getScene().getWindow();
        stage.close();
    }

    private int generateRequestId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(RESCUE_REQUESTS_CSV_FILE))) {
            String line;
            boolean isFirstLine = true;  // Flag to skip the header

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;  // Skip the header line
                    continue;
                }

                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0]);
                if (id > maxId) {
                    maxId = id;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

}
