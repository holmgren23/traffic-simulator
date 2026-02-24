package com.trafficsimulator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class VehicleTest {
    Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = new Vehicle(1, 1, 1);
    }
    
    @Test
    public void testGetID() {
        assertEquals(1, vehicle.getID());
    }
}
