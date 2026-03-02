package com.trafficsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;


class TrafficSimulatorTest {
    private TrafficSimulator simulator;
    
    //Randommizer seed, Triggers on 4096;
    int seed = 4095;

    @BeforeEach
    void setUp() {
        //Seed 4096 triggers the random probability of slowdown on the first tick
        //Any seed before 4096 won't trigger it
        
        simulator = new TrafficSimulator(new Random(seed));
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

    @Test
    void testSpawnCar_spawnDoNotWrongTick() {
        simulator.tickUpdate();

        assertNull(simulator.getRoad().getCell(0));
    }

    @Test
    void testSpawnCar_spawnOnCorrectTick() {
        for (int i = 0; i < simulator.getSpawnRate(); i++) {
            simulator.tickUpdate();
        }

        assertNotNull(simulator.getRoad().getCell(0));
    }

    @Test
    void testSpawnCar_noSpawnIfCellOccupied() {
        Car blockingCar = new Car(99, 0, 5);
        simulator.getRoad().setCell(0, blockingCar);

        for (int i = 0; i < 5; i++) {
            simulator.tickUpdate();
        }

        assertEquals(blockingCar, simulator.getRoad().getCell(0));
    }

    @Test
    void testMoveCars_carMovesForwardOnOpenRoad() {
        Car car = new Car(0, 0, 5);
        simulator.getRoad().setCell(0, car);
        simulator.getCars().add(car);

        simulator.tickUpdate();

        if (seed == 4096) {
            //Slowdown triggered
            assertEquals(4, car.getPosition());
        }else{
            assertEquals(5, car.getPosition());
        }
    }

    @Test
    void testUpdateSpeeds_carBreaksWhenCarCloseAhead() {
        Car car1 = new Car(0, 0, 5);
        Car car2 = new Car(1, 2, 5);

        simulator.getRoad().setCell(0, car1);
        simulator.getRoad().setCell(2, car2);
        simulator.getCars().add(car1);
        simulator.getCars().add(car2);

        simulator.tickUpdate();

        assertEquals(1, car1.getPosition());
    }

    @Test
    void updateSpeeds_speedCannotExceedMaxSpeed() {
        Car car = new Car(0, 0, 3);
        car.setCurrentSpeed(3); // already at max
        simulator.getRoad().setCell(0, car);
        simulator.getCars().add(car);

        for (int i = 0; i < 20; i++) {
            simulator.tickUpdate();
            assertTrue(car.getCurrentSpeed() <= car.getMaxSpeed());
        }
    }

    @Test
    void updateSpeeds_speedCannotExceedGap() {
        Car car1 = new Car(0, 0, 5);
        Car car2 = new Car(1, 3, 5); // gap of 2
        simulator.getRoad().setCell(0, car1);
        simulator.getRoad().setCell(3, car2);
        simulator.getCars().add(car1);
        simulator.getCars().add(car2);

        simulator.tickUpdate();

        // car1 should never have moved past car2
        assertTrue(car1.getPosition() < car2.getPosition());
    }

    @Test
    void testMoveCars_carRemovedWhenExitsRoad() {
        int atEnd = simulator.getRoad().getLength() - 1;
        Car car = new Car(0, atEnd, 5);
        car.setCurrentSpeed(5);
        simulator.getRoad().setCell(atEnd, car);
        simulator.getCars().add(car);

        simulator.tickUpdate();

        assertEquals(0, simulator.getCars().size());
    }
}