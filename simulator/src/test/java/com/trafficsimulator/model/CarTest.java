package com.trafficsimulator.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class CarTest {
    Car car;

    @BeforeEach
    public void setUp() {
        car = new Car(1, 2, 3, 0.2);
    }
    
    @AfterEach void cleanUp() {
        car = null;
    }
    
    @Test
    public void testGetID() {
        assertEquals(1, car.getID());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(2, car.getPosition());
    }
    
    @Test
    public void testGetCurrentSpeed() {
        assertEquals(3, car.getCurrentSpeed());
    }
    
    @Test
    public void testSetCurrentSpeed() {
        car.setCurrentSpeed(5);
        assertEquals(5, car.getCurrentSpeed());
    }

    @Test
    public void testSetMaxSpeed(){
        car.setMaxSpeed(6);
        assertEquals(6, car.getMaxSpeed());
    }

    @Test
    public void testSetPosition(){
        car.setPosition(4);
        assertEquals(4, car.getPosition());
    }

    @Test
    public void testgetLength(){
        assertEquals(1, car.getLength());
    }

    @Test
public void testCarIsVehicle() {
    assertTrue(car instanceof Vehicle);
}

@Test
public void testCarIsRoadObject() {
    assertTrue(car instanceof RoadObject);
}
}
