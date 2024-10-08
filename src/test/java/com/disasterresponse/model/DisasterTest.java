package com.disasterresponse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // Import assertions like assertEquals

public class DisasterTest {

    private Disaster disaster;

    @BeforeEach // Use BeforeEach for JUnit 5
    public void setUp() {
        // Initialize a Disaster object before each test
        disaster = new Disaster("New York", "Hurricane", "Critical", "Active", "Severe flooding", "2023-09-10 12:30:00");
    }

    @Test
    public void testGetLocation() {
        assertEquals("New York", disaster.getLocation());
    }

    @Test
    public void testGetType() {
        assertEquals("Hurricane", disaster.getType());
    }

    @Test
    public void testGetSeverity() {
        assertEquals("Critical", disaster.getSeverity());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Active", disaster.getStatus());
    }

    @Test
    public void testSetStatus() {
        disaster.setStatus("Inactive");
        assertEquals("Inactive", disaster.getStatus());
    }

    @Test
    public void testGetComment() {
        assertEquals("Severe flooding", disaster.getComment());
    }

    @Test
    public void testGetReportedTime() {
        assertEquals("2023-09-10 12:30:00", disaster.getReportedTime());
    }
}
