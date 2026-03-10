package com.trafficsimulator.utility;

public class InputParametersDTO {
  private double spawnRate, minSpeed, maxSpeed;
  private Integer roadLength;
  private String mapType;

  public InputParametersDTO(
    double spawnRate,
    Integer roadLength,
    double minSpeed,
    double maxSpeed,
    String mapType
  ) {
    this.spawnRate = spawnRate;
    this.roadLength = roadLength;
    this.minSpeed = minSpeed;
    this.maxSpeed = maxSpeed;
    this.mapType = mapType;
  }
  
  public double getSpawnRate() {
    return this.spawnRate;
  }
  
  public Integer getRoadLength() {
    return this.roadLength;
  }
  
  public double getMinSpeed() {
    return this.minSpeed;
  }
  
  public double getMaxSpeed() {
    return this.maxSpeed;
  }

  public String getMapType() {
    return this.mapType;
  }
}
