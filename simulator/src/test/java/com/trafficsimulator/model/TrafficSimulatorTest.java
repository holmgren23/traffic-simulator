package com.trafficsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class TrafficSimulatorTest {
    private TrafficSimulator simulator;
    @BeforeEach

    void setUp() {
        simulator = new TrafficSimulator();
    }

    @Test
    void gap_carImmediatelyAhead() {
        simulator.getRoad().setCell(0, new Car(0, 0, 5));
        simulator.getRoad().setCell(1, new Car(1, 1, 5));

        assertEquals(0, simulator.gap(0));
    }
}