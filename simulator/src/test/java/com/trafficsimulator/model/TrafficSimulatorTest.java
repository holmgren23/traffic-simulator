package com.trafficsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trafficsimulator.model.Road.Direction;

import java.util.Random;


class TrafficSimulatorTest {
    private TrafficSimulator simulator;
    private Road mainRoad;
    
    //Randommizer seed, Triggers on 4096;
    int seed = 4095;

    @BeforeEach
    void setUp() {
        //Seed 4096 triggers the random probability of slowdown on the first tick
        //Any seed before 4096 won't trigger it
        
        mainRoad = new Road(200, Road.Direction.EAST, 0, 50, 1000, 50, 8);
        Map map = new Map(MapType.ONE_WAY);
        map.addRoad(mainRoad);
        simulator = new TrafficSimulator(new Random(seed), map);
    }

    @Test
    void testGap_noCarAhead() {
        mainRoad.setCell(0, new Car(0, 0, 5, 0.5));

        assertEquals(Integer.MAX_VALUE, simulator.getMap().gap(mainRoad, 0));
    }

    @Test
    void testGap_carImmediatelyAhead() {
        mainRoad.setCell(0, new Car(0, 0, 5, 0.5));
        mainRoad.setCell(1, new Car(1, 1, 5, 0.5));

        assertEquals(0, simulator.getMap().gap(mainRoad, 0));
    }

    @Test
    void testGap_carThreeCellsAhead() {
        mainRoad.setCell(0, new Car(0, 0, 5, 0.5));
        mainRoad.setCell(3, new Car(1, 3, 5, 0.5));

        assertEquals(2, simulator.getMap().gap(mainRoad, 0));
    }

    @Test
    void testGap_CarAtLastCell() {
        int lastcell = mainRoad.getLength() - 1;
        mainRoad.setCell(lastcell, new Car(0, lastcell, 5, 0.5));

        assertEquals(Integer.MAX_VALUE, simulator.getMap().gap(mainRoad, lastcell));
    }

    @Test
    void testSpawnCar_spawnDoNotWrongTick() {
        simulator.tickUpdate();

        assertNull(mainRoad.getCell(0));
    }

    @Test
    void testSpawnCar_spawnOnCorrectTick() {
        simulator.setSpawnProbability(1);
        simulator.tickUpdate();

        assertNotNull(mainRoad.getCell(0));
    }

    @Test
    void testSpawnCar_noSpawnIfCellOccupied() {
        Car blockingCar = new Car(99, 0, 5, 0.5);
        mainRoad.setCell(0, blockingCar);

        for (int i = 0; i < 5; i++) {
            simulator.tickUpdate();
        }

        assertEquals(blockingCar, mainRoad.getCell(0));
    }

    @Test
    void testMoveCars_carMovesForwardOnOpenRoad() {
        Car car = new Car(0, 0, 5, 0.2);
        mainRoad.setCell(0, car);
        car.setRoad(mainRoad);
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
        Car car1 = new Car(0, 0, 5, 0.5);
        Car car2 = new Car(1, 2, 5, 0.5);
        car1.setRoad(mainRoad);
        car2.setRoad(mainRoad);
        mainRoad.setCell(0, car1);
        mainRoad.setCell(2, car2);
        simulator.getCars().add(car1);
        simulator.getCars().add(car2);

        simulator.tickUpdate();

        assertEquals(1, car1.getPosition());
    }

    @Test
    void updateSpeeds_speedCannotExceedMaxSpeed() {
        Car car = new Car(0, 0, 3, 0.2);
        car.setRoad(mainRoad);
        car.setCurrentSpeed(3); //already at max
        mainRoad.setCell(0, car);
        simulator.getCars().add(car);

        for (int i = 0; i < 20; i++) {
            simulator.tickUpdate();
            assertTrue(car.getCurrentSpeed() <= car.getMaxSpeed());
        }
    }

    @Test
    void updateSpeeds_speedCannotExceedGap() {
        Car car1 = new Car(0, 0, 5, 0.5);
        Car car2 = new Car(1, 3, 5, 0.5); //gap of 2
        car1.setRoad(mainRoad);
        car2.setRoad(mainRoad);
        mainRoad.setCell(0, car1);
        mainRoad.setCell(3, car2);
        simulator.getCars().add(car1);
        simulator.getCars().add(car2);

        simulator.tickUpdate();

        //car1 should never have moved past car2
        assertTrue(car1.getPosition() < car2.getPosition());
    }

    @Test
    void testLaneChange_counterIncreasesWhenCarChangesLane() {
        // Setup two parallel roads where the car can overtake
        Road slowLane = new Road(200);
        Road fastLane = new Road(200);
        slowLane.setOvertakeLane(fastLane);
        fastLane.setMergeLane(slowLane);

        Map map = new Map(MapType.HIGHWAY);
        map.addRoad(slowLane);
        map.addRoad(fastLane);
        TrafficSimulator sim = new TrafficSimulator(new Random(seed), map);

        // Place a blocking car ahead and a fast car behind it on the slow lane
        Car blocker = new Car(0, 3, 1);
        blocker.setRoad(slowLane);
        blocker.setCurrentSpeed(1);
        slowLane.setCell(3, blocker);
        sim.getCars().add(blocker);

        Car overtaker = new Car(1, 0, 5);
        overtaker.setRoad(slowLane);
        overtaker.setCurrentSpeed(0);
        slowLane.setCell(0, overtaker);
        sim.getCars().add(overtaker);

        int laneChangesBefore = sim.getStats().getSnapshot().totalLaneChanges();

        // Run enough ticks to trigger a lane change
        for (int i = 0; i < 10; i++) {
            sim.tickUpdate();
        }

        int laneChangesAfter = sim.getStats().getSnapshot().totalLaneChanges();
        assertTrue(laneChangesAfter > laneChangesBefore, "Lane change counter should increase after cars switch lanes");
    }

    @Test
    void testMoveCars_carRemovedWhenExitsRoad() {
        int atEnd = mainRoad.getLength() - 1;
        Car car = new Car(0, atEnd, 5, 0.5);
        car.setRoad(mainRoad);
        car.setCurrentSpeed(5);
        mainRoad.setCell(atEnd, car);
        simulator.getCars().add(car);

        simulator.tickUpdate();

        assertEquals(0, simulator.getCars().size());
    }
}