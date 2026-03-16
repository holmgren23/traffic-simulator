package com.trafficsimulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsTest {

    private Statistics stats;

    @BeforeEach
    public void setUp() {
        stats = new Statistics();
    }

    // Verify that the statistics update correctly with an empty list of cars
    @Test
    public void testUpdateWithEmptyList() {
        // Setup with an empty list of cars and zero total spawned
        List<Car> emptyList = new ArrayList<>();
        int totalSpawned = 0;

        // Update
        stats.update(emptyList, totalSpawned);
        StatSnapshot snapshot = stats.getSnapshot();

        // Check that values are zero for an empty simulation
        assertEquals(0, snapshot.currentCars());
        assertEquals(0, snapshot.totalSpawned());
        assertEquals(0.0, snapshot.currentAvgSpeedKmh());
        assertEquals(0, snapshot.stationaryCars());
    }

    // Verify that average speed and stationary car count are calculated correctly
    @Test
    public void testAverageSpeedAndStationaryCars() {
        // Setup of cars with different speeds
        Car car1 = new Car(1, 5, 5, 0.5);
        car1.setCurrentSpeed(2); // speed 2

        Car car2 = new Car(2, 0, 5, 0.5);
        car2.setCurrentSpeed(0); // speed 0

        List<Car> cars = List.of(car1, car2);

        int totalSpawned = 3; 

        // Update
        stats.update(cars, totalSpawned);
        StatSnapshot snapshot = stats.getSnapshot();

        // Assertions
        assertEquals(2, snapshot.currentCars()); // should be 2 cars currently in the simulation
        assertEquals(3, snapshot.totalSpawned()); // should reflect the total spawned cars
        assertEquals(1, snapshot.stationaryCars()); // should count 1 stationary car
        
        // should be 2 * 30 / 2 = 30.0 km/h average speed, since car 1 has speed 2 and car 2 has speed 0
        assertEquals(10.0, snapshot.currentAvgSpeedKmh());
    }

    @Test
    public void testFlowRatesOverTime() {
        // Setup: simulate 1 minute of updates with 5 cars spawned and 1 car still on the road
        Car carOnRoad = new Car(1, 0, 5, 0.2);
        carOnRoad.setCurrentSpeed(5);
        List<Car> cars = List.of(carOnRoad); // so far only 1 car is on the road

        int totalSpawned = 5; // total 5 cars have spawned during this time, but only 1 is currently on the road (4 have exited)
        
        // Simulate 600 ticks (1 minute) of updates
        for (int i = 0; i < 600; i++) {
            stats.update(cars, totalSpawned);
        }
        // Get snapshot after 1 minute
        StatSnapshot snapshot = stats.getSnapshot();

        // Ticks should be 600 
        assertEquals(600, snapshot.totalTicks());
        
 
        // If 5 cars have spawned in 1 minute, the average inflow should be 5.0 vehicles per minute
        assertEquals(5.0, snapshot.avgInFlow());
        
        // If 5 cars have spawned and only 1 is still on the road, then 4 have exited, the average outflow should be 4.0
        assertEquals(4.0, snapshot.avgOutFlow());
    }

    @Test
    public void testFinalReportFormat() {
        // setup with some cars and stats
        List<Car> emptyList = new ArrayList<>();
        // Updates stats with 10 total spawned cars, but no cares are currently on the road(exited)
        stats.update(emptyList, 10); 

        // Get the final report string
        String report = stats.getFinalReport();

        // Check that the report contains the expected summary information
        org.junit.jupiter.api.Assertions.assertTrue(report.contains("--- FINAL REPORT ---"));
        org.junit.jupiter.api.Assertions.assertTrue(report.contains("Total Cars Spawned: 10"));
        org.junit.jupiter.api.Assertions.assertTrue(report.contains("Total Cars Exited: 10"));
    }


}