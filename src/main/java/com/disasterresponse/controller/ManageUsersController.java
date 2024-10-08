package com.disasterresponse.controller;

import com.disasterresponse.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersController {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button assignRoleButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private Button backButton;

    private static final String CSV_FILE_PATH = "src/main/resources/com/csv/users.csv";
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private User selectedUser;

    public void initialize() {
        // Bind username and role columns to User class
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));

        loadUsersData();

        // Update ComboBox with selected user's role
        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = newValue;
            if (newValue != null) {
                roleComboBox.setValue(newValue.getRole());
            }
        });

        // Populate roleComboBox with available roles
        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Emergency Responders", "Agencies or Organization", "Public"));
    }

    private void loadUsersData() {
        userData.clear(); // Clear the current data
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                String[] fields = line.split(",");
                if (fields.length >= 8) {  // Ensure we have at least 8 columns
                    String userId = fields[0];
                    String username = fields[4]; // Username from the 5th column
                    String password = fields[6]; // Password from the 7th column
                    String role = fields[7];     // Role from the 8th column (AccessLevel)

                    // Add user to the ObservableList
                    userData.add(new User(userId, username, password, role));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the items in the table
        userTable.setItems(userData);
    }

    @FXML
    protected void handleAssignRoleAction() {
        if (selectedUser != null && roleComboBox.getValue() != null) {
            selectedUser.setRole(roleComboBox.getValue());
            updateUserRoleInFile(selectedUser);
            refreshTable();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user and role before assigning.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void handleDeleteUserAction() {
        if (selectedUser != null) {
            deleteUserFromFile(selectedUser);
            loadUsersData(); // Refresh user list after deletion
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user before deleting.");
            alert.showAndWait();
        }
    }

    private void updateUserRoleInFile(User updatedUser) {
        List<String[]> users = new ArrayList<>();

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
                if (fields[4].equals(updatedUser.getUsername())) {  // Match username (from the 5th column)
                    fields[7] = updatedUser.getRole();  // Update the role (AccessLevel)
                }
                users.add(fields);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back the updated data to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("userId,FullName,Address,PhoneNumber,Username,Email,Password,AccessLevel");
            writer.newLine();
            for (String[] user : users) {
                writer.write(String.join(",", user));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserFromFile(User userToDelete) {
        List<String[]> users = new ArrayList<>();

        // Read the CSV and collect all rows except the one to delete
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                String[] fields = line.split(",");
                if (!fields[4].equals(userToDelete.getUsername())) {  // Match username
                    users.add(fields);  // Keep all other users
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back the updated data to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.write("userId,FullName,Address,PhoneNumber,Username,Email,Password,AccessLevel");
            writer.newLine();
            for (String[] user : users) {
                writer.write(String.join(",", user));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
            Parent root = loader.load();

            HomepageController controller = loader.getController();
            controller.initializePage();  // Reinitialize homepage

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        userTable.refresh();
    }
}
