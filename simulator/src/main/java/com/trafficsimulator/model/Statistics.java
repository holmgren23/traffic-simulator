package com.trafficsimulator.model;

import java.util.List;

import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.utility.HelperMethods;

public class Statistics {
    private int totalCarsSpawned = 0;
    private int totalCarsExited = 0;
    private long totalTicks = 0;
    private long totalElapsedTimeMS = 0;
    private double currentAvgSpeed = 0;
    private int currentStationary = 0;
    private int currentCarCount = 0;
    private int minSpeed, maxSpeed, spawnRate, roadLength;
    private MapType mapType;

    public Statistics() {}

    public Statistics(int minSpeed, int maxSpeed, int spawnRate, int roadLength, MapType mapType) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.spawnRate = spawnRate;
        this.roadLength = roadLength;
        this.mapType = mapType;
    }

    public void update(List<Car> cars, int totalSpawned) {

        totalTicks++;
        totalElapsedTimeMS += Constants.TICK_LENGTH;
        this.currentCarCount = cars.size();
        this.totalCarsSpawned = totalSpawned;
        this.totalCarsExited = totalSpawned - this.currentCarCount;
        
       // If there are no cars, average speed and stationary count is zero
        if (cars.isEmpty()) {
            this.currentAvgSpeed = 0;
            this.currentStationary = 0;
            return;
        }

        // Calculate average speed and count stationary cars
        double speedSum = 0;
        int stoppedCount = 0;
        for (Car car : cars) {
            double speed = car.getCurrentSpeed();
            speedSum += speed;
            if (speed == 0) stoppedCount++;
        }
        // Update current stats
        this.currentStationary = stoppedCount;
        this.currentAvgSpeed = speedSum / cars.size();
    }

    // Generates a snapshot of the current statistics
    public StatSnapshot getSnapshot() {
        double totalMinutes = (totalElapsedTimeMS) / 60000.0;
        // Calculate average inflow and outflow rates
        double avgInFlow = (totalMinutes > 0) ? (totalCarsSpawned / totalMinutes) : 0;
        double avgOutFlow = (totalMinutes > 0) ? (totalCarsExited / totalMinutes) : 0;
        // Create and return a snapshot of the current statistics
        return new StatSnapshot(
            totalCarsSpawned,
            currentCarCount,
            currentStationary,
            HelperMethods.speedToKMH(currentAvgSpeed),
            avgInFlow,
            avgOutFlow,
            totalTicks,
            totalElapsedTimeMS,
            minSpeed,
            maxSpeed,
            spawnRate,
            roadLength,
            mapType
        );
    }

    // Generates a final report string summarizing the simulation results
    public String getFinalReport() {
        StatSnapshot s = getSnapshot();
        long totalSeconds = totalElapsedTimeMS / 1000;
        
        return String.format(
            "--- FINAL REPORT ---\n" +
            "Time Elapsed: %02d:%02d:%02d\n" +
            "Total Cars Spawned: %d\n" +
            "Total Cars Exited: %d\n" +
            "Average Speed: %.2f km/h\n" +
            "InFlow: %.2f veh/min\n" +
            "OutFlow: %.2f veh/min",

            totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60,
            s.totalSpawned(), totalCarsExited,
            s.currentAvgSpeedKmh(), s.avgInFlow(), s.avgOutFlow()
        );
    }
    
    public String getSettingsReport() {
        return String.format(
            "--- SETTINGS ---\n"+
            "Map: %s\n" +
            "Min Speed: %d km/h\n" +
            "Max Speed: %d kmh/h\n" +
            "Spawn Rate: %d\n" +
            "Road Length: %d" +
            " \n" +
            " \n",
            mapType, (int)HelperMethods.speedToKMH(minSpeed), (int)HelperMethods.speedToKMH(maxSpeed), spawnRate, roadLength
        );
    }

}
