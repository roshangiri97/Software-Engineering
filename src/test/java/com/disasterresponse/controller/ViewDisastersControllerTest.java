package com.disasterresponse.controller;

import com.disasterresponse.model.Disaster;
import dao.DisasterDAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar.ButtonData;
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
public class ViewDisastersControllerTest {

    private ViewDisastersController controller;
    private Stage stage;

    // Custom test implementation of DisasterDAO
    private class TestDisasterDAO extends DisasterDAO {
        @Override
        public List<Disaster> getAllDisasters() throws SQLException {
            // Return a predefined list of disasters for testing
            List<Disaster> disasters = new ArrayList<>();
            disasters.add(new Disaster("Test Location 1", "Fire", "High", "Active", "Test Comment 1", "2024-10-13 12:00:00"));
            disasters.add(new Disaster("Test Location 2", "Flood", "Medium", "Under Control", "Test Comment 2", "2024-10-13 13:00:00"));
            return disasters;
        }
    }

    @Start
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/ViewDisastersView.fxml"));
        Parent root = loader.load();

        // Get the controller
        controller = loader.getController();

        // Inject the test DisasterDAO
        controller.disasterDAO = new TestDisasterDAO();

        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Keep a reference to the stage
        this.stage = stage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize the UI components
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initialize(null, null);
            }
        });

        // Wait for JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();
    }

    @Test
    public void testLoadDisasterReports(FxRobot robot) {
        // Wait for the disasters to be loaded
        WaitForAsyncUtils.waitForFxEvents();

        // Get the VBox containing the disaster reports
        VBox disastersVBox = controller.disastersVBox;

        // Check that the VBox has the correct number of children
        assertEquals(2, disastersVBox.getChildren().size(), "Disasters VBox should contain 2 children.");

        // Verify the contents of the first disaster report
        VBox firstDisasterBox = (VBox) disastersVBox.getChildren().get(0);
        assertNotNull(firstDisasterBox, "First disaster VBox should not be null.");

        Label locationLabel = (Label) firstDisasterBox.getChildren().get(0);
        assertEquals("Location: Test Location 1", locationLabel.getText());

        Label typeLabel = (Label) firstDisasterBox.getChildren().get(1);
        assertEquals("Disaster Type: Fire", typeLabel.getText());

        Label timeLabel = (Label) firstDisasterBox.getChildren().get(2);
        assertEquals("Disaster Reported: 2024-10-13 12:00:00", timeLabel.getText());

        Label severityLabel = (Label) firstDisasterBox.getChildren().get(3);
        assertEquals("Severity: High", severityLabel.getText());

        Label statusLabel = (Label) firstDisasterBox.getChildren().get(4);
        assertEquals("Status: Active", statusLabel.getText());

        Label commentsLabel = (Label) firstDisasterBox.getChildren().get(5);
        assertEquals("Description: Test Comment 1", commentsLabel.getText());

        // Similarly, verify the contents of the second disaster report
        VBox secondDisasterBox = (VBox) disastersVBox.getChildren().get(1);
        assertNotNull(secondDisasterBox, "Second disaster VBox should not be null.");

        locationLabel = (Label) secondDisasterBox.getChildren().get(0);
        assertEquals("Location: Test Location 2", locationLabel.getText());

        typeLabel = (Label) secondDisasterBox.getChildren().get(1);
        assertEquals("Disaster Type: Flood", typeLabel.getText());

        timeLabel = (Label) secondDisasterBox.getChildren().get(2);
        assertEquals("Disaster Reported: 2024-10-13 13:00:00", timeLabel.getText());

        severityLabel = (Label) secondDisasterBox.getChildren().get(3);
        assertEquals("Severity: Medium", severityLabel.getText());

        statusLabel = (Label) secondDisasterBox.getChildren().get(4);
        assertEquals("Status: Under Control", statusLabel.getText());

        commentsLabel = (Label) secondDisasterBox.getChildren().get(5);
        assertEquals("Description: Test Comment 2", commentsLabel.getText());
    }

    @Test
    public void testHandleBackToHomeAction(FxRobot robot) {
        // Simulate clicking the "Back to Home" button
        robot.clickOn("#backToHomeButton");

        // Wait for the navigation to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Check that the scene has changed
        Parent root = stage.getScene().getRoot();
        assertNotNull(root, "Scene should have a root node after navigation.");

        // Optionally, verify that the new scene corresponds to the homepage
        // You can check for specific UI elements or controllers
    }

    @Test
    public void testErrorHandlingInLoadDisasterReports(FxRobot robot) {
        // Replace the disasterDAO with one that throws an exception
        controller.disasterDAO = new DisasterDAO() {
            @Override
            public List<Disaster> getAllDisasters() throws SQLException {
                throw new SQLException("Test exception");
            }
        };

        // Re-initialize the controller to trigger the loading
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initialize(null, null);
            }
        });
        WaitForAsyncUtils.waitForFxEvents();

        // Since an alert is shown, attempt to capture it
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertNotNull(dialogPane, "An error alert should be displayed");

        // Check the content of the alert
        String alertContent = dialogPane.getContentText();
        assertEquals("An error occurred while fetching disaster reports from the database.", alertContent);

        // Simulate clicking "OK" on the alert dialog
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        assertNotNull(okButton, "OK button should be present in the alert dialog");
        robot.clickOn(okButton);

        // Ensure that no disasters are displayed
        assertEquals(0, controller.disastersVBox.getChildren().size(), "No disasters should be displayed when there is an error.");
    }
}
