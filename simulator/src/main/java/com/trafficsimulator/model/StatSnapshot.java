package com.trafficsimulator.model;

public record StatSnapshot(
    int totalSpawned,
    int currentCars,
    int stationaryCars,
    double currentAvgSpeedKmh,
    double avgInFlow,
    double avgOutFlow,
    long totalTicks
) {}