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
    void testGap_noCarAhead() {
        simulator.getRoad().setCell(0, new Car(0, 0, 5));

        assertEquals(Integer.MAX_VALUE, simulator.gap(0));
    }

    @Test
    void testGap_carImmediatelyAhead() {
        simulator.getRoad().setCell(0, new Car(0, 0, 5));
        simulator.getRoad().setCell(1, new Car(1, 1, 5));

        assertEquals(0, simulator.gap(0));
    }

    @Test
    void testGap_carThreeCellsAhead() {
        simulator.getRoad().setCell(0, new Car(0, 0, 5));
        simulator.getRoad().setCell(3, new Car(1, 3, 5));

        assertEquals(2, simulator.gap(0));
    }

    @Test
    void testGap_CarAtLastCell() {
        int lastcell = simulator.getRoad().getLength() - 1;
        simulator.getRoad().setCell(lastcell, new Car(0, lastcell, 5));

        assertEquals(Integer.MAX_VALUE, simulator.gap(lastcell));
    }
}