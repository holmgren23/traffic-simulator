package com.trafficsimulator.model;

// Record class to hold a snapshot of traffic statistics at a given moment
public record StatSnapshot(
    int totalSpawned,
    int currentCars,
    int stationaryCars,
    double currentAvgSpeedKmh,
    double avgInFlow,
    double avgOutFlow,
    long totalTicks,
    long totalElapsedTimeMS,
    int minSpeed,
    int maxSpeed,
    int spawnRate,
    int roadLength
) {}