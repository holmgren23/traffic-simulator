package com.trafficsimulator.utility;

public record InputParametersDTO(
    String mapType,
    Integer roadLength,
    double minSpeed,
    double maxSpeed,
    double spawnRate,
    double timeScalingFactor,
    double timeScalingRaw,
    boolean runOffline,
    double ticksToRun 
  ) {
}
