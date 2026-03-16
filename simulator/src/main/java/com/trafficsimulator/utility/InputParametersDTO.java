package com.trafficsimulator.utility;

/**
 * Data transfer object (DTO) used to send values from
 * user settings in the settings menu to the controller
 * Has automatic constructor and getters
 */
public record InputParametersDTO(
    String mapType,
    Integer roadLength,
    double minSpeed,
    double maxSpeed,
    double spawnPercent,
    double timeScalingFactor,
    double timeScalingRaw,
    boolean runOffline,
    double ticksToRun 
  ) {
}
