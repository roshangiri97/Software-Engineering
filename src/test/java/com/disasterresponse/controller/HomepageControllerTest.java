package com.disasterresponse.controller;

import com.disasterresponse.model.SessionManager;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class HomepageControllerTest {

    public HomepageController homepageController;
    public Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/HomepageView.fxml"));
        Parent root = loader.load();

        // Get the controller
        homepageController = loader.getController();

        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Keep a reference to the stage
        this.stage = stage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Reset the session manager before each test
        SessionManager.getInstance().clearSession();

        // Set a logged-in user
        SessionManager.getInstance().setLoggedInUser("1", "admin", "Admin", "Administrator");

        // Initialize the page (which sets up the welcome label and userRole)
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
            homepageController.initializePage();
        }
    });
        // Wait for the JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testLogout(FxRobot robot) {
        // Verify that a user is logged in
        assertNotNull(SessionManager.getInstance().getUsername(), "User should be logged in before logout.");

        // Simulate clicking the logout button
        robot.clickOn("#logoutButton");

        // Wait for the alert to be shown
        WaitForAsyncUtils.waitForFxEvents();

        // Simulate clicking "OK" on the alert dialog
        robot.clickOn("OK");

        // Wait for the logout action to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the session has been cleared
        assertNull(SessionManager.getInstance().getUsername(), "User should be logged out after logout.");
    }
}
