package com.disasterresponse.controller;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.IncidentReport;
import com.disasterresponse.model.SessionManager;
import dao.IncidentReportDAO;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;
import org.testfx.framework.junit5.Start;
import java.sql.*;
import java.time.LocalDate;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class IncidentReportControllerTest {

    public IncidentReportController controller;
    public Stage stage;

    public String testRequestId = "99999"; // A unique request ID for testing

   @Start
    public void start(Stage stage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/disasterresponse/view/IncidentReportForm.fxml"));
        Parent root = loader.load();

        // Get the controller
        controller = loader.getController();

        // Set up the scene and stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        this.stage = stage;
    }

     @BeforeEach
    public void setUp() throws Exception {
        // Prepare the test data in the database
        insertTestRescueRequest(testRequestId);

        // Set a logged-in user in SessionManager
        SessionManager.getInstance().setLoggedInUser("1", "testuser", "Rescue Department", "Test User");

        // Initialize the form with the test request ID
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.initializeForm(testRequestId);
            }
        });

        // Wait for JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();
    }

   @AfterEach
    public void tearDown() throws Exception {
        // Clean up the test data from the database
        deleteTestRescueRequest(testRequestId);
        deleteIncidentReport(testRequestId);

        // Clear the SessionManager
        SessionManager.getInstance().clearSession();

        // Close the stage on the JavaFX Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.close();
            }
        });

        // Wait for JavaFX events to be processed
        WaitForAsyncUtils.waitForFxEvents();
    }
    @Test
    public void testInitializeForm() throws Exception {
        // Verify that the UI fields have been populated with the data from the rescue request
        assertEquals("Test Location", controller.locationField.getText(), "Location field should be populated from rescue request");
        assertEquals("Fire", controller.typeField.getText(), "Type field should be populated from rescue request");
        assertEquals("Rescue Department", controller.teamsInvolvedField.getText(), "Teams Involved field should be populated from rescue request");

        // Verify that reportDateField is set to current date
        String expectedDate = LocalDate.now().toString();
        assertEquals(expectedDate, controller.reportDateField.getText(), "Report date should be set to current date");
    }

    @Test
    public void testHandleSubmitReportAction(FxRobot robot) throws Exception {
        // Set input values
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.evacuationsField.setText("10");
                controller.rescuedField.setText("5");
                controller.casualtiesField.setText("1");
                controller.propertyDamageField.setText("Minor damage to buildings");
                controller.infrastructureImpactField.setText("Roads blocked");
                controller.reliefActionsField.setText("Provided medical aid");
                controller.witnessStatementField.setText("Witness saw the fire start");
                controller.reportDateField.setText("2024-10-13");
            }
        });

        // Wait for JavaFX events
        WaitForAsyncUtils.waitForFxEvents();

        // Simulate clicking the submit button
        robot.clickOn("#submitReportButton");

        // Wait for the submission to complete
        WaitForAsyncUtils.waitForFxEvents();

        // Verify that the incident report has been saved to the database
        IncidentReport report = getIncidentReportByRequestId(testRequestId);
        assertNotNull(report, "Incident report should be saved to the database");

        assertEquals(10, report.getEvacuations());
        assertEquals(5, report.getRescued());
        assertEquals(1, report.getCasualties());
        assertEquals("Minor damage to buildings", report.getPropertyDamage());
        assertEquals("Roads blocked", report.getInfrastructureImpact());
        assertEquals("Provided medical aid", report.getReliefActions());
        assertEquals("Witness saw the fire start", report.getWitnessStatement());
        assertEquals(Date.valueOf("2024-10-13"), report.getReportDate());

        // Verify that the stage is closed
        assertFalse(stage.isShowing(), "Stage should be closed after submission");
    }

    @Test
    public void testConvertReportDate() {
        Date date = controller.convertReportDate("2024-10-13");
        assertEquals(Date.valueOf("2024-10-13"), date, "convertReportDate should return correct Date object");

        // Test invalid date format
        Date invalidDate = controller.convertReportDate("invalid-date");
        assertNull(invalidDate, "convertReportDate should return null for invalid date string");
    }

    // Helper methods for database interactions

    public void insertTestRescueRequest(String requestId) throws SQLException {
        String query = "INSERT INTO rescue_requests (request_id, location, disasterType, departments) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(requestId));
            stmt.setString(2, "Test Location");
            stmt.setString(3, "Fire");
            stmt.setString(4, "Rescue Department");
            stmt.executeUpdate();
        }
    }

    public void deleteTestRescueRequest(String requestId) throws SQLException {
        String query = "DELETE FROM rescue_requests WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(requestId));
            stmt.executeUpdate();
        }
    }

    public void deleteIncidentReport(String requestId) throws SQLException {
        String query = "DELETE FROM incident_reports WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(requestId));
            stmt.executeUpdate();
        }
    }

    public IncidentReport getIncidentReportByRequestId(String requestId) throws SQLException {
        String query = "SELECT * FROM incident_reports WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(requestId));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create an IncidentReport object from the result set
                IncidentReport report = new IncidentReport(
                        rs.getInt("request_id"),
                        rs.getString("location"),
                        rs.getString("type"),
                        rs.getInt("evacuations"),
                        rs.getInt("rescued"),
                        rs.getInt("casualties"),
                   rs.getString("property_damage"), // Corrected
                    rs.getString("infrastructure_impact"), // Corrected
                    rs.getString("relief_actions"), // Corrected
                    rs.getString("teams_involved"), // Corrected
                    rs.getString("witness_statement"), // Corrected
                    rs.getDate("report_date") // Corrected
                );
                return report;
            }
        }
        return null;
    }
@AfterEach
public void afterEachTest() throws Exception {
    // Close the stage on the JavaFX Application Thread
    Platform.runLater(new Runnable() {
        @Override
        public void run() {
            stage.close();
        }
    });

    // Wait for JavaFX events to be processed
    WaitForAsyncUtils.waitForFxEvents();

    // Perform any additional cleanup if necessary
}

}
