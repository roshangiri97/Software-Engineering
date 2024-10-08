/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.disasterresponse.model;

/**
 *
 * @author 12229277
 */
import java.sql.Connection;  // Import the Connection class
import java.sql.DriverManager;  // Import the DriverManager class
import java.sql.SQLException;  // Import the SQLException class

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/drs";  // Your database details
    private static final String USER = "root";  // Replace with your MySQL username
    private static final String PASSWORD = "pass";  // Replace with your MySQL password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}