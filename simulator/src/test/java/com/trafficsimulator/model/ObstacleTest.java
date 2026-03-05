package com.trafficsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObstacleTest {
    Obstacle obstacle;

    @BeforeEach
    public void setUp() {
        obstacle = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 5);
    }

    @AfterEach
    public void cleanUp() {
        obstacle = null;
    }

    @Test
    public void testGetPosition() {
        assertEquals(5, obstacle.getPosition());
    }

    @Test
    public void testSetPosition() {
        obstacle.setPosition(10);
        assertEquals(10, obstacle.getPosition());
    }

    @Test
    public void testGetType() {
        assertEquals(ObstacleType.TRAFFIC_LIGHT, obstacle.getType());
    }

    @Test
    public void testIsOpenDefaultFalse() {
        assertFalse(obstacle.isOpen());
    }

}
