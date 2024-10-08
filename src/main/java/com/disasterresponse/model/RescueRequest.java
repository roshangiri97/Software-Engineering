package com.disasterresponse.model;

public class RescueRequest {

    private int requestId;
    private String location;
    private String disasterType;
    private String status;
    private String departments;
    private String additionalInstructions;

    public RescueRequest(int requestId, String location, String disasterType, String status, String departments, String additionalInstructions) {
        this.requestId = requestId;
        this.location = location;
        this.disasterType = disasterType;
        this.status = status;
        this.departments = departments;
        this.additionalInstructions = additionalInstructions;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartments() {
        return departments;
    }

    public void setDepartments(String departments) {
        this.departments = departments;
    }

    public String getAdditionalInstructions() {
        return additionalInstructions;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }
}
