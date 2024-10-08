package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private ObservableList<User> userData = FXCollections.observableArrayList();
    private User selectedUser;

    public void initialize() {
        usernameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));

        loadUsersData();

        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUser = newValue;
            if (newValue != null) {
                roleComboBox.setValue(newValue.getRole());
            }
        });

        roleComboBox.setItems(FXCollections.observableArrayList("Admin", "Emergency Responders", "Agencies or Organization", "Public"));
    }

    private void loadUsersData() {
        userData.clear();
        String query = "SELECT userId, Username, AccessLevel FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String userId = rs.getString("userId");
                String username = rs.getString("Username");
                String role = rs.getString("AccessLevel");

                userData.add(new User(userId, username, "", role));
            }

            userTable.setItems(userData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleAssignRoleAction() {
        if (selectedUser != null && roleComboBox.getValue() != null) {
            selectedUser.setRole(roleComboBox.getValue());
            updateUserRoleInDatabase(selectedUser);
            loadUsersData();  // Reload the updated data after role assignment
        } else {
            showAlert("No Selection", "Please select a user and role.");
        }
    }

    private void updateUserRoleInDatabase(User updatedUser) {
        String query = "UPDATE users SET AccessLevel = ? WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updatedUser.getRole());
            stmt.setString(2, updatedUser.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleDeleteUserAction() {
        if (selectedUser != null) {
            deleteUserFromDatabase(selectedUser);
            loadUsersData();  // Refresh after deletion
        } else {
            showAlert("No Selection", "Please select a user to delete.");
        }
    }

    private void deleteUserFromDatabase(User userToDelete) {
        String query = "DELETE FROM users WHERE Username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userToDelete.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleBackAction() {
        loadView("/com/disasterresponse/view/HomepageView.fxml");
    }

    private void loadView(String viewPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
