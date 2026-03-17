package com.trafficsimulator.model;

/**
 * Snapshot of traffic statistics captured at a single point in time.
 * Used for exporting data to CSV and displaying results in {@link com.trafficsimulator.view.StatisticsView}.
 *
 * @param totalSpawned       Total number of cars spawned since the simulation started
 * @param currentCars        Number of cars currently active on the map
 * @param stationaryCars     Number of cars currently standing still (speed = 0)
 * @param currentAvgSpeedKmh Average speed of all active cars in km/h
 * @param avgInFlow          Average number of cars entering the map per tick
 * @param avgOutFlow         Average number of cars leaving the map per tick
 * @param totalTicks         Total number of simulation ticks elapsed
 * @param totalElapsedTimeMS Total real-world time elapsed in milliseconds
 * @param minSpeed           Minimum car ideal speed (cells/tick)
 * @param maxSpeed           Maximum car ideal speed (cells/tick)
 * @param spawnProbability   Probability of spawning a car each tick (0–1)
 * @param roadLength         Length of each road segment in cells
 * @param mapType            The type of map layout used in this simulation
 * @param peakCarCount       Highest number of cars simultaneously on the map
 * @param minAvgSpeedKmh     Lowest average speed recorded during the simulation, in km/h
 * @param totalLaneChanges   Total number of lane changes performed by all cars
 */
public record StatSnapshot(
    long totalSpawned,
    int currentCars,
    int stationaryCars,
    double currentAvgSpeedKmh,
    double avgInFlow,
    double avgOutFlow,
    long totalTicks,
    long totalElapsedTimeMS,
    int minSpeed,
    int maxSpeed,
    double spawnProbability,
    int roadLength,
    String mapType,
    int peakCarCount,
    double minAvgSpeedKmh,
    long totalLaneChanges
) {}