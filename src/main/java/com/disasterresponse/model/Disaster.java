package com.disasterresponse.model;

public class Disaster {

    private String location;
    private String type;
    private String severity;
    private String status;
    private String comment;
    private String reportedTime; // New field

    // Constructor
    public Disaster(String location, String type, String severity, String status, String comment, String reportedTime) {
        this.location = location;
        this.type = type;
        this.severity = severity;
        this.status = status;
        this.comment = comment;
        this.reportedTime = reportedTime; // Initialize reported time
    }

    // Getters
    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getReportedTime() { // New getter for reported time
        return reportedTime;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
}
