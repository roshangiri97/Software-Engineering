package com.disasterresponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "drs";
    private static final String USER = "root"; // Replace with your DB user
    private static final String PASSWORD = "pass"; // Replace with your DB password

    // Establish a connection to the MySQL server (without specifying the database)
    private static Connection getServerConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Establish a connection to the specific database
    public static Connection getConnection() throws SQLException {
        createDatabaseIfNotExists();
        return DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
    }

    // Method to create the database if it doesn't exist
    private static void createDatabaseIfNotExists() {
        try (Connection connection = getServerConnection(); Statement statement = connection.createStatement()) {
            // Create the database if it doesn't exist
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(createDatabaseSQL);

            // Use the newly created or existing database
            String useDatabaseSQL = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseSQL);

            // Create the Disasters table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Disasters (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "location VARCHAR(255) NOT NULL, " +
                    "type VARCHAR(100) NOT NULL, " +
                    "severity VARCHAR(50) NOT NULL, " +
                    "status VARCHAR(50) NOT NULL, " +
                    "comment TEXT, " +
                    "reportedTime DATETIME NOT NULL" +
                    ")";
            statement.executeUpdate(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
