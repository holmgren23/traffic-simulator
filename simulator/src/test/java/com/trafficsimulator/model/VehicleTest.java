package com.trafficsimulator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class VehicleTest {
    Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = new Vehicle(1, 2, 3);
    }
    
    @AfterEach void cleanUp() {
        vehicle = null;
    }
    
    @Test
    public void testGetID() {
        assertEquals(1, vehicle.getID());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(2, vehicle.getPosition());
    }
    
    @Test
    public void testGetCurrentSpeed() {
        assertEquals(3, vehicle.getCurrentSpeed());
    }
    
    @Test
    public void testSetCurrentSpeed() {
        vehicle.setCurrentSpeed(5);
        assertEquals(5, vehicle.getCurrentSpeed());
    }

    @Test
    public void testSetMaxSpeed(){
        vehicle.setMaxSpeed(6);
        assertEquals(6, vehicle.getMaxSpeed());
    }

    @Test
    public void testSetPosition(){
        vehicle.setPosition(4);
        assertEquals(4, vehicle.getPosition());
    }

    @Test
    public void testgetLength(){
        assertEquals(1, vehicle.getLength());
    }
}
