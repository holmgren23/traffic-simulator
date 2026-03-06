package com.trafficsimulator.model;

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