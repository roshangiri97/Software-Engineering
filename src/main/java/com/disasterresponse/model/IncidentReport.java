/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.disasterresponse.model;

import java.sql.Date;

/**
 *
 * @author Lenovo
 */
public class IncidentReport {
    private int requestId; // Corresponds to request_id
    private int userId; // Corresponds to userId
    private String location; // Corresponds to location
    private String type; // Corresponds to type
    private int evacuations; // Corresponds to evacuations
    private int rescued; // Corresponds to rescued
    private int casualties; // Corresponds to casualties
    private String propertyDamage; // Corresponds to property_damage
    private String infrastructureImpact; // Corresponds to infrastructure_impact
    private String reliefActions; // Corresponds to relief_actions
    private String teamsInvolved; // Corresponds to teams_involved
    private String witnessStatement; // Corresponds to witness_statement
    private Date reportDate; // Corresponds to report_date

    // Constructor
    public IncidentReport(int requestId, int userId, String location, String type, int evacuations, int rescued,
                          int casualties, String propertyDamage, String infrastructureImpact, String reliefActions,
                          String teamsInvolved, String witnessStatement, Date reportDate) {
        this.requestId = requestId;
        this.userId = userId;
        this.location = location;
        this.type = type;
        this.evacuations = evacuations;
        this.rescued = rescued;
        this.casualties = casualties;
        this.propertyDamage = propertyDamage;
        this.infrastructureImpact = infrastructureImpact;
        this.reliefActions = reliefActions;
        this.teamsInvolved = teamsInvolved;
        this.witnessStatement = witnessStatement;
        this.reportDate = reportDate;
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEvacuations() {
        return evacuations;
    }

    public void setEvacuations(int evacuations) {
        this.evacuations = evacuations;
    }

    public int getRescued() {
        return rescued;
    }

    public void setRescued(int rescued) {
        this.rescued = rescued;
    }

    public int getCasualties() {
        return casualties;
    }

    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    public String getPropertyDamage() {
        return propertyDamage;
    }

    public void setPropertyDamage(String propertyDamage) {
        this.propertyDamage = propertyDamage;
    }

    public String getInfrastructureImpact() {
        return infrastructureImpact;
    }

    public void setInfrastructureImpact(String infrastructureImpact) {
        this.infrastructureImpact = infrastructureImpact;
    }

    public String getReliefActions() {
        return reliefActions;
    }

    public void setReliefActions(String reliefActions) {
        this.reliefActions = reliefActions;
    }

    public String getTeamsInvolved() {
        return teamsInvolved;
    }

    public void setTeamsInvolved(String teamsInvolved) {
        this.teamsInvolved = teamsInvolved;
    }

    public String getWitnessStatement() {
        return witnessStatement;
    }

    public void setWitnessStatement(String witnessStatement) {
        this.witnessStatement = witnessStatement;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    // Override toString method for easier debugging
    @Override
    public String toString() {
        return "IncidentReport{" +
                "requestId=" + requestId +
                ", userId=" + userId +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", evacuations=" + evacuations +
                ", rescued=" + rescued +
                ", casualties=" + casualties +
                ", propertyDamage='" + propertyDamage + '\'' +
                ", infrastructureImpact='" + infrastructureImpact + '\'' +
                ", reliefActions='" + reliefActions + '\'' +
                ", teamsInvolved='" + teamsInvolved + '\'' +
                ", witnessStatement='" + witnessStatement + '\'' +
                ", reportDate=" + reportDate +
                '}';
    }
}
