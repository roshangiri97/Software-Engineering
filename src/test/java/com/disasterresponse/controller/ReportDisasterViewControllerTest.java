package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import com.disasterresponse.model.SessionManager;
import dao.DisasterDAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class ReportDisasterViewControllerTest {

    private ReportDisasterViewController controller;
    private Stage stage;

    // Test implementation of DisasterDAO
    private class TestDisasterDAO extends DisasterDAO {
        private List<Disaster> savedDisasters = new ArrayList<>();

        @Override
        public void saveDisaster(Disaster disaster) throws SQLException {
            // Instead of saving to the database, add to the list
            savedDisasters.add(disaster);
        }

        public List<Disaster> getSavedDisasters() {
            return savedDisasters;
        }
    }

    // Instance of the test DAO
    private TestDisasterDAO testDisasterDAO;

    @Start
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/ReportDisasterView.fxml"));
        Parent root = loader.load();

        // Get the controller
        controller = loader.getController();

        // Inject the test DisasterDAO
        testDisasterDAO = new TestDisasterDAO();
        controller.disasterDAO = testDisasterDAO;

        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Keep a reference to the stage
        this.stage = stage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Reset the SessionManager before each test
        SessionManager.getInstance().clearSession();

        // Set a logged-in user (if needed)
        SessionManager.getInstance().setLoggedInUser("1", "testuser", "General Public", "Test User");

        // Initialize the UI components
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initialize();
            }
        });

        // Wait for JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();

        // Clear any previously saved disasters
        testDisasterDAO.getSavedDisasters().clear();
    }

    @Test
public void testSuccessfulDisasterReportSubmission(FxRobot robot) throws SQLException {
    // Set valid input values
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            controller.disasterTypeComboBox.setValue("Fire");
            controller.locationField.setText("123 Main St");
            controller.severityComboBox.setValue("High");
            controller.commentsArea.setText("Test comment");
        }
    });
    WaitForAsyncUtils.waitForFxEvents();

    // Simulate clicking the submit button
    robot.clickOn("#submitButton");

    // Wait for the alert to appear
    WaitForAsyncUtils.waitForFxEvents();

    // Simulate clicking "OK" on the alert dialog
    robot.clickOn("OK");

    // Wait for any pending events
    WaitForAsyncUtils.waitForFxEvents();

    // Verify that the disaster was "saved"
    assertEquals(1, testDisasterDAO.getSavedDisasters().size(), "One disaster should be saved.");
    Disaster savedDisaster = testDisasterDAO.getSavedDisasters().get(0);
    assertEquals("Fire", savedDisaster.getType());
    assertEquals("123 Main St", savedDisaster.getLocation());
    assertEquals("High", savedDisaster.getSeverity());
    assertEquals("Test comment", savedDisaster.getComment());

    // Check that the success label is updated appropriately
    Label successLabel = controller.successLabel;
    assertEquals("Success: Disaster report submitted.", successLabel.getText());

    // Verify that the input fields are cleared
    assertEquals("", controller.locationField.getText());
    assertEquals("Hurricane", controller.disasterTypeComboBox.getValue());
    assertEquals("Medium", controller.severityComboBox.getValue());
    assertEquals("", controller.commentsArea.getText());
}

    @Test
    public void testDisasterReportSubmissionWithMissingFields(FxRobot robot) throws SQLException {
        // Set invalid input values (missing location)
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.disasterTypeComboBox.setValue("Fire");
                controller.locationField.setText("");
                controller.severityComboBox.setValue("High");
                controller.commentsArea.setText("Test comment");
            }
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Simulate clicking the submit button
        robot.clickOn("#submitButton");

        // Wait for the submission attempt to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that no disasters were "saved"
        assertEquals(0, testDisasterDAO.getSavedDisasters().size(), "No disasters should be saved.");

        // Check that the success label displays an error message
        Label successLabel = controller.successLabel;
        assertEquals("Error: Please fill in all the required fields.", successLabel.getText());
    }

    @Test
    public void testHandleCancelAction(FxRobot robot) throws Exception {
        // Simulate clicking the cancel button
        robot.clickOn("#cancelButton");

        // Wait for the navigation to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Check that the scene has changed
        Parent root = stage.getScene().getRoot();
        assertNotNull(root, "Scene should have a root node after cancel action.");

        // Optionally, you can check whether the controller is now an instance of HomepageController
    }
}
