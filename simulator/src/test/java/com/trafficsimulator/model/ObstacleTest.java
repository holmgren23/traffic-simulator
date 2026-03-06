package com.trafficsimulator.model;

<<<<<<< HEAD
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
=======
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObstacleTest {

    private Obstacle obs;

    @BeforeEach
        void setUp() {
        obs = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 0);
    }

    @Test
    void testTrafficLight_openOnFirstTick() {
        obs.calculatePriority(1);
        assertTrue(obs.isOpen());
    }

    @Test
    void testTrafficLight_openDuringGreenPhase() {
        obs.calculatePriority(19); //last green tick
        assertTrue(obs.isOpen());
    }

    @Test
    void testTrafficLight_closedDuringRedPhase() {
        obs.calculatePriority(20); //first red tick
        assertFalse(obs.isOpen());
    }

    @Test
    void testTrafficLight_cyclesCorrectly() {
        obs.calculatePriority(40); //back to green
        assertTrue(obs.isOpen());
    }
}
>>>>>>> feature/intersection
