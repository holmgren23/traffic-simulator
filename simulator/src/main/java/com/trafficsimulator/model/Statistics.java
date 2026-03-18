package com.trafficsimulator.model;

import java.util.List;

import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.utility.HelperMethods;

/**
 * Tracks and calculates traffic statistics during a simulation.
 * Updated every tick via {@link #update(java.util.List, int)},
 * and can produce a {@link StatSnapshot} at any point in time.
 * <p>
 * In addition to current-state metrics, this class tracks peak values
 * and cumulative counters such as peak car count, minimum average speed,
 * and total number of lane changes performed.
 */
public class Statistics {
    private long totalCarsSpawned = 0;
    private long totalCarsExited = 0;
    private long totalTicks = 0;
    private long totalElapsedTimeMS = 0;
    private double currentAvgSpeed = 0;
    private int currentStationary = 0;
    private int currentCarCount = 0;
    private int minSpeed, maxSpeed, roadLength;
    private double spawnProbability;
    private String mapType;
    private int peakCarCount = 0;
    private double minAvgSpeed = Double.MAX_VALUE;
    private long totalLaneChanges = 0;
    private long startTime;

    /** Creates an empty Statistics object with no simulation settings. */
    public Statistics() {}

    /**
     * Creates a Statistics object with the given simulation settings.
     * @param minSpeed   Minimum maxspeed when spawning cars (cells/tick)
     * @param maxSpeed   Maximum maxspeed when spawning cars (cells/tick)
     * @param spawnProbability  Probability of spawning new cars each tick
     * @param roadLength Length of each road segment in cells
     * @param mapType    The map layout used in current simulation
     */
    public Statistics(int minSpeed, int maxSpeed, double spawnProbability, int roadLength, String mapType) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.spawnProbability = spawnProbability;
        this.roadLength = HelperMethods.cellsToM(roadLength);
        this.mapType = mapType;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Updates statistics for the current tick.
     * Increments tick count, recalculates average speed, counts stationary cars,
     * and updates peak car count and minimum average speed records.
     * @param cars         All cars currently active on the map
     * @param totalSpawned Total number of cars spawned since simulation start
     */
    public void update(List<Car> cars, int totalSpawned) {

        totalTicks++;
        totalElapsedTimeMS += HelperMethods.ticksToMs(1);
        this.currentCarCount = cars.size();
        this.totalCarsSpawned = totalSpawned;
        this.totalCarsExited = totalSpawned - this.currentCarCount;

        // Track peak number of simultaneous cars
        if (currentCarCount > peakCarCount) peakCarCount = currentCarCount;

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

        // Track minimum average speed seen during the simulation
        if (currentAvgSpeed < minAvgSpeed) minAvgSpeed = currentAvgSpeed;
    }

    /**
     * Registers a single lane change event.
     * Should be called each time a car successfully switches lanes.
     */
    public void registerLaneChange() {
        totalLaneChanges++;
    }

    /**
     * Creates an immutable snapshot of the current statistics.
     * Inflow and outflow rates are calculated as vehicles per minute.
     * Includes peak car count, minimum average speed, and total lane changes.
     * @return A {@link StatSnapshot} representing the current state
     */
    public StatSnapshot getSnapshot() {
        double totalMinutes = (totalElapsedTimeMS) / 60000.0;
        // Calculate average inflow and outflow rates
        double avgInFlow = (totalMinutes > 0) ? (totalCarsSpawned / totalMinutes) : 0;
        double avgOutFlow = (totalMinutes > 0) ? (totalCarsExited / totalMinutes) : 0;
        double resolvedMinAvgSpeed = (minAvgSpeed == Double.MAX_VALUE) ? 0 : minAvgSpeed;
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
            spawnProbability,
            roadLength,
            mapType,
            peakCarCount,
            HelperMethods.speedToKMH(resolvedMinAvgSpeed),
            totalLaneChanges
        );
    }

    /**
     * Generates a formatted multi-line report string with final simulation results.
     * Includes elapsed time, car counts, average speed, and flow rates.
     * @return A human-readable report string
     */
    public String getFinalReport() {
        StatSnapshot s = getSnapshot();
        long totalSeconds = totalElapsedTimeMS / 1000;
        
        return String.format(
            "--- FINAL REPORT ---\n" +
            "Simulated Time: %02d:%02d:%02d\n" +
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
    
    /**
     * Generates a formatted multi-line string listing the simulation settings.
     * Speeds are converted to km/h for readability.
     * @return A human-readable settings string
     */
    public String getSettingsReport() {
        return String.format(
            "--- SETTINGS ---\n"+
            "Map: %s\n" +
            "Preferred Car Speed: %d-%d km/h\n" +
            "Spawn Probability: %d%%/tick\n" +
            "Road Length: %s" +
            " \n" +
            " \n" +
            "Runtime: %s \n",
            mapType,
            (int)HelperMethods.speedToKMH(minSpeed),
            (int)HelperMethods.speedToKMH(maxSpeed),
            HelperMethods.probabilityToPercent(spawnProbability),
            HelperMethods.roadLengthToString(mapType, roadLength),
            this.getRuntime()
        );
    }

    private String getRuntime() {
        long t = (System.currentTimeMillis() - this.startTime) / 1000;
        return String.format(
            "%02d:%02d:%02d",
            (int)(t / 3600),
            (int)((t % 3600) / 60),
            (int)(t % 60)
        );
    }
}
