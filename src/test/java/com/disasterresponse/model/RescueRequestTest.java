package com.disasterresponse.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RescueRequestTest {

    private RescueRequest rescueRequest;

    @BeforeEach
    public void setUp() {
        // Initialize a RescueRequest object before each test
        rescueRequest = new RescueRequest(1, "Miami", "Hurricane", "Help on board", "Fire Department;EMS", "Send boats");
    }

    @Test
    public void testGetRequestId() {
        assertEquals(1, rescueRequest.getRequestId());
    }

    @Test
    public void testGetLocation() {
        assertEquals("Miami", rescueRequest.getLocation());
    }

    @Test
    public void testGetDisasterType() {
        assertEquals("Hurricane", rescueRequest.getDisasterType());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Help on board", rescueRequest.getStatus());
    }

    @Test
    public void testSetStatus() {
        rescueRequest.setStatus("Under control");
        assertEquals("Under control", rescueRequest.getStatus());
    }

    @Test
    public void testGetDepartments() {
        assertEquals("Fire Department;EMS", rescueRequest.getDepartments());
    }

    @Test
    public void testGetAdditionalInstructions() {
        assertEquals("Send boats", rescueRequest.getAdditionalInstructions());
    }
}
