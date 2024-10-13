/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import com.disasterresponse.DatabaseConnection;
import com.disasterresponse.model.IncidentReport;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lenovo
 */
public class IncidentReportDAO {

    public void saveIncidentReport(IncidentReport report) {
        String query = "INSERT INTO incident_reports (request_id, location, type, evacuations, rescued, casualties, property_damage, infrastructure_impact, relief_actions, teams_involved, witness_statement, report_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the parameters using the IncidentReport model
            // Set the parameters using the IncidentReport model
             stmt.setInt(1, report.getRequestId());
        stmt.setString(2, report.getLocation()); // Corrected here
        stmt.setString(3, report.getType());
        stmt.setInt(4, report.getEvacuations());
        stmt.setInt(5, report.getRescued());
        stmt.setInt(6, report.getCasualties());
        stmt.setString(7, report.getPropertyDamage());
        stmt.setString(8, report.getInfrastructureImpact());
        stmt.setString(9, report.getReliefActions());
        stmt.setString(10, report.getTeamsInvolved());
        stmt.setString(11, report.getWitnessStatement());
        stmt.setDate(12, report.getReportDate());

            // Execute the insert
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Method to get all incident reports
    public List<IncidentReport> getAllIncidentReports() {
        List<IncidentReport> incidentReports = new ArrayList<>();
        String query = "SELECT * FROM incident_reports";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int request_id = rs.getInt("request_id");
              
                String location = rs.getString("location");
                String type = rs.getString("type");
                int evacuations = rs.getInt("evacuations");
                int rescued = rs.getInt("rescued");
                int casualties = rs.getInt("casualties");
                String propertyDamage = rs.getString("property_damage");
                String infrastructureImpact = rs.getString("infrastructure_impact");
                String reliefActions = rs.getString("relief_actions");
                String teamsInvolved = rs.getString("teams_involved");
                String witnessStatement = rs.getString("witness_statement");
                Date reportDate = rs.getDate("report_date");

                // Create an IncidentReport object and add it to the list
                IncidentReport report = new IncidentReport(request_id, location, type, evacuations, rescued,
                        casualties, propertyDamage, infrastructureImpact, reliefActions, teamsInvolved,
                        witnessStatement, reportDate);
                incidentReports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incidentReports;
    }
}
