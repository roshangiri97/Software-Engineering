package com.disasterresponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "drs";
    private static final String USER = "root"; // Replace with your DB user
    private static final String PASSWORD = "root"; // Replace with your DB password

    // Establish a connection to the MySQL server (without specifying the database)
    private static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Establish a connection to the specific database
    public static Connection getConnection() throws SQLException {
        createDatabaseIfNotExists();
        return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
    }

    // Method to create the database and tables if they don't exist
    private static void createDatabaseIfNotExists() {
        try (Connection connection = getServerConnection(); Statement statement = connection.createStatement()) {

            // Create the database if it doesn't exist
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDatabaseSQL);

            // Use the newly created or existing database
            String useDatabaseSQL = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseSQL);

            // Create the users table if it doesn't exist
            String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "userId INT AUTO_INCREMENT PRIMARY KEY, "
                    + "FullName VARCHAR(255), "
                    + "Address VARCHAR(255), "
                    + "PhoneNumber VARCHAR(50), "
                    + "Username VARCHAR(100) UNIQUE, "
                    + "Email VARCHAR(100) UNIQUE, "
                    + "Password VARCHAR(255), "
                    + "AccessLevel VARCHAR(50))";
            statement.executeUpdate(createUsersTableSQL);

            // Create the disaster_reports table if it doesn't exist
            String createDisasterReportsTableSQL = "CREATE TABLE IF NOT EXISTS disaster_reports ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "userId INT, "
                    + "type VARCHAR(255), "
                    + "location VARCHAR(255), "
                    + "severity VARCHAR(50), "
                    + "comment TEXT, "
                    + "reportedTime DATETIME, "
                    + "status VARCHAR(50), "
                    + "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)";
            statement.executeUpdate(createDisasterReportsTableSQL);

            // Create the rescue_requests table if it doesn't exist
            String createRescueRequestsTableSQL = "CREATE TABLE IF NOT EXISTS rescue_requests ("
                    + "request_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "location VARCHAR(255), "
                    + "disasterType VARCHAR(100), "
                    + "status VARCHAR(50), "
                    + "departments TEXT, "
                    + "additionalInstructions TEXT)";
            statement.executeUpdate(createRescueRequestsTableSQL);

            // Create the incident_reports table if it doesn't exist
            String createIncidentReportsTableSQL = "CREATE TABLE IF NOT EXISTS incident_reports ("
                    + "request_id INT, "
                    + "userId INT, "
                    + "location VARCHAR(255), "
                    + "type VARCHAR(100), "
                    + "evacuations INT, "
                    + "rescued INT, "
                    + "casualties INT, "
                    + "property_damage TEXT, "
                    + "infrastructure_impact TEXT, "
                    + "relief_actions TEXT, "
                    + "teams_involved TEXT, "
                    + "witness_statement TEXT, "
                    + "report_date DATE, "
                    + "FOREIGN KEY (request_id) REFERENCES rescue_requests(request_id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE)";
            statement.executeUpdate(createIncidentReportsTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
