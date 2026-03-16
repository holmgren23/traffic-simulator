package com.trafficsimulator.model;

import org.junit.jupiter.api.Test;

import com.trafficsimulator.model.Road.Direction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


public class RoadTest {
    private Road road;

    @BeforeEach
    public void setup() {
        road = new Road(100, Road.Direction.EAST, 0, 50, 500, 50, 8);
    }
    @AfterEach
    public void tearDown() {
        road = null;
    }
    
    @Test
    public void testgetLength() {
        assertEquals(100, road.getLength());
    }
    
    @Test
    public void getCellEmpty() {
        assertEquals(null,road.getCell(5));
    }
    
    @Test
    public void testsetCellgetCell() {
        RoadObject car = new Car(1, 0, 0, 0.2);
        road.setCell(10, car);
        assertEquals(car, road.getCell(10));
    }

}
