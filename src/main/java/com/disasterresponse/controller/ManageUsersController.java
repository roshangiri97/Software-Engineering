package com.disasterresponse.controller;

import com.disasterresponse.model.User;
import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private UserDAO userDAO = new UserDAO();

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

        roleComboBox.setItems(FXCollections.observableArrayList("admin", "Emergency Responders", "Agencies or Organization", "Public"));
    }

    private void loadUsersData() {
        userData.clear();
        List<User> users = userDAO.getAllUsers();
        userData.addAll(users);
        userTable.setItems(userData);
    }

    @FXML
    protected void handleAssignRoleAction() {
        if (selectedUser != null && roleComboBox.getValue() != null) {
            selectedUser.setRole(roleComboBox.getValue());
             userDAO.updateUserRoleInDatabase(selectedUser);
            loadUsersData();  // Reload the updated data after role assignment
        } else {
            showAlert("No Selection", "Please select a user and role.");
        }
    }

    @FXML
    protected void handleDeleteUserAction() {
        if (selectedUser != null) {
            userDAO.deleteUserByUsername(selectedUser.getUsername());
            loadUsersData();  // Refresh after deletion
        } else {
            showAlert("No Selection", "Please select a user to delete.");
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
