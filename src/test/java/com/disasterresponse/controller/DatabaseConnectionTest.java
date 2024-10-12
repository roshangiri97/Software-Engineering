package com.disasterresponse.controller;

import com.disasterresponse.model.DatabaseConnection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseConnectionTest {

    private static Connection connection;

    @BeforeAll
    public static void setUp() throws SQLException {
        // Establish the connection before all tests
        connection = DatabaseConnection.getConnection();
    }

    @AfterAll
    public static void tearDown() throws SQLException {
        // Clean up after all tests
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    public void testConnection() {
        // Test that the connection is not null
        assertNotNull(connection, "Database connection should not be null.");
    }

    @Test
    public void testUsersTableExists() throws SQLException {
        // Test that the users table exists
        String query = "SHOW TABLES LIKE 'users'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertTrue(resultSet.next(), "The 'users' table should exist.");
        }
    }

    @Test
    public void testDisasterReportsTableExists() throws SQLException {
        // Test that the disaster_reports table exists
        String query = "SHOW TABLES LIKE 'disaster_reports'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertTrue(resultSet.next(), "The 'disaster_reports' table should exist.");
        }
    }

    @Test
    public void testRescueRequestsTableExists() throws SQLException {
        // Test that the rescue_requests table exists
        String query = "SHOW TABLES LIKE 'rescue_requests'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertTrue(resultSet.next(), "The 'rescue_requests' table should exist.");
        }
    }

    @Test
    public void testIncidentReportsTableExists() throws SQLException {
        // Test that the incident_reports table exists
        String query = "SHOW TABLES LIKE 'incident_reports'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            assertTrue(resultSet.next(), "The 'incident_reports' table should exist.");
        }
    }
}
