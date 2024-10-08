package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.Disaster;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private ComboBox<String> sortComboBox;
    @FXML
    private Button backButton;

    private ObservableList<Disaster> disasterData = FXCollections.observableArrayList();

    public void initialize() {
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        severityColumn.setCellValueFactory(new PropertyValueFactory<>("severity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadDisasterDataFromDatabase();

        sortComboBox.setItems(FXCollections.observableArrayList("Recently Added", "Severity", "Location (A-Z)", "Type"));

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
                        updateDisasterStatusInDatabase(disaster);
                        refreshTable();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
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
                    openSendAssistanceForm(disaster);
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

    private void loadDisasterDataFromDatabase() {
        disasterData.clear();
        String query = "SELECT location, type, severity, status, comment, reportedTime FROM disaster_reports";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String location = rs.getString("location");
                String type = rs.getString("type");
                String severity = rs.getString("severity");
                String status = rs.getString("status");
                String comment = rs.getString("comment");
                String reportedTime = rs.getString("reportedTime");

                disasterData.add(new Disaster(location, type, severity, status, comment, reportedTime));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleSortSelection() {
        String selectedSort = sortComboBox.getValue();

        if (selectedSort != null) {
            switch (selectedSort) {
                case "Recently Added":
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getReportedTime).reversed());
                    break;
                case "Severity":
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getSeverity));
                    break;
                case "Location (A-Z)":
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getLocation));
                    break;
                case "Type":
                    FXCollections.sort(disasterData, Comparator.comparing(Disaster::getType));
                    break;
            }
            disasterTable.refresh();
        }
    }

    private void updateDisasterStatusInDatabase(Disaster updatedDisaster) {
        String query = "UPDATE disaster_reports SET status = ? WHERE location = ? AND type = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedDisaster.getStatus());
            stmt.setString(2, updatedDisaster.getLocation());
            stmt.setString(3, updatedDisaster.getType());

            stmt.executeUpdate();

        } catch (SQLException e) {
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

            HomepageController controller = loader.getController();
            controller.initializePage();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSendAssistanceForm(Disaster disaster) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/SendAssistanceForm.fxml"));
            Parent root = loader.load();

            SendAssistanceController controller = loader.getController();
            controller.initializeForm(disaster);

            Stage stage = new Stage();
            stage.setTitle("Send Assistance");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
