package com.disasterresponse.model;

public class Disaster {
    private int id; // New field for primary key
    private String location;
    private String type;
    private String severity;
    private String status;
    private String comment;
    private String reportedTime;

    // Constructor with ID
    public Disaster(int id, String location, String type, String severity, String status, String comment, String reportedTime) {
        this.id = id;
        this.location = location;
        this.type = type;
        this.severity = severity;
        this.status = status;
        this.comment = comment;
        this.reportedTime = reportedTime;
    }

    // Constructor without ID (for new entries)
    public Disaster(String location, String type, String severity, String status, String comment, String reportedTime) {
        this.location = location;
        this.type = type;
        this.severity = severity;
        this.status = status;
        this.comment = comment;
        this.reportedTime = reportedTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getReportedTime() {
        return reportedTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
