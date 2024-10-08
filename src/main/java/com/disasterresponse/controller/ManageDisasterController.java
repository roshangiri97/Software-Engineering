package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;

public class ManageDisasterController {

    @FXML
    private TableView<Disaster> disasterTable;

    @FXML
    private TableColumn<Disaster, String> locationColumn;
    @FXML
    private TableColumn<Disaster, String> typeColumn;
    @FXML
    private TableColumn<Disaster, String> severityColumn;
    @FXML
    private TableColumn<Disaster, String> statusColumn;
    @FXML
    private TableColumn<Disaster, Void> actionColumn;
    @FXML
    private TableColumn<Disaster, Void> assistanceColumn;

    @FXML
    private ComboBox<String> sortComboBox;  // ComboBox for sorting options
    @FXML
    private Button backButton;

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/disaster_reports.csv";
    private ObservableList<Disaster> disasterData = FXCollections.observableArrayList();

    public void initialize() {
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadDisasterData();

        // Initialize ComboBox options for sorting
        sortComboBox.setItems(FXCollections.observableArrayList("Recently Added", "Severity", "Location (A-Z)", "Type"));

        // Handle sorting when an option is selected from ComboBox
        sortComboBox.setOnAction(event -> handleSortSelection());

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            private final Button changeButton = new Button("Change Status");
            private final HBox actionBox = new HBox(10, comboBox, changeButton);

            {
                comboBox.getItems().addAll("Active", "Help on board", "Under control", "Inactive");

                changeButton.setOnAction(event -> {
                    Disaster disaster = getTableView().getItems().get(getIndex());
                    String newStatus = comboBox.getValue();
                    if (newStatus != null) {
                        disaster.setStatus(newStatus);
                        updateDisasterStatusInFile(disaster);
                        refreshTable();
                    } else {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Invalid Selection");
                        alert.setHeaderText("No Status Selected");
                        alert.setContentText("Please select a status before changing.");
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Disaster disaster = getTableView().getItems().get(getIndex());
                    comboBox.setValue(disaster.getStatus());
                    setGraphic(actionBox);
                }
            }
        });

        assistanceColumn.setCellFactory(param -> new TableCell<>() {
            private final Button sendButton = new Button("Send Assistance");

            {
                sendButton.setOnAction(event -> {
                    Disaster disaster = getTableView().getItems().get(getIndex());
                    openSendAssistanceForm(disaster); // Open the form to send assistance
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(sendButton);
                }
            }
        });

        disasterTable.setItems(disasterData);
    }

    private void handleSortSelection() {
        String selectedSort = sortComboBox.getValue();

        if (selectedSort != null) {
            switch (selectedSort) {
                case "Recently Added":
                    // Sort by recently added (newest disasters first)
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getReportedTime).reversed());
                    break;
                case "Severity":
                    // Sort by severity
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getSeverity));
                    break;
                case "Location (A-Z)":
                    // Sort by location in alphabetical order
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getLocation));
                    break;
                case "Type":
                    // Sort by disaster type in alphabetical order
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getType));
                    break;
            }
            disasterTable.refresh();  // Refresh the table to apply the sorting
        }
    }

    private void loadDisasterData() {
        disasterData.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header line
                }

                String[] fields = line.split(",");
                if (fields.length >= 8) {  // Ensure enough fields exist
                    String location = fields[3];
                    String disasterType = fields[2];
                    String severity = fields[4];
                    String status = fields[7];
                    String comment = fields[5];
                    String reportedTime = fields[6]; // Ensure reportedTime is included in the CSV

                    disasterData.add(new Disaster(location, disasterType, severity, status, comment, reportedTime));  // Pass reportedTime
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDisasterStatusInFile(Disaster updatedDisaster) {
        List<String[]> disasters = new ArrayList<>();

        // Read the CSV and collect all rows
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }

                String[] fields = line.split(",");
                if (fields[3].equals(updatedDisaster.getLocation()) && fields[2].equals(updatedDisaster.getType())) {
                    fields[7] = updatedDisaster.getStatus(); // Update status
                }
                disasters.add(fields);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back the updated data to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("id,userId,DisasterType,Location,Severity,Comments,reportedTime,status");
            writer.newLine();
            for (String[] disaster : disasters) {
                writer.write(String.join(",", disaster));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        disasterTable.refresh();
    }

    @FXML
    protected void handleBackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            // Re-initialize the homepage to load the recent alerts and buttons
            HomepageController controller = loader.getController();
            controller.initializePage();  // Ensure the homepage is fully reinitialized

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openSendAssistanceForm(Disaster disaster) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/SendAssistanceForm.fxml"));
            Parent root = loader.load();

            // Pass the selected disaster to the SendAssistanceController
            SendAssistanceController controller = loader.getController();
            controller.initializeForm(disaster);

            Stage stage = new Stage();
            stage.setTitle("Send Assistance");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
