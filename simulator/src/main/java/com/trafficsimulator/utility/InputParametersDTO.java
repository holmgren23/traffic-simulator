package com.trafficsimulator.utility;

public class InputParametersDTO {
  private double spawnRate;
  private Integer roadLength;

  public InputParametersDTO(double spawnRate, Integer roadLength) {
    this.spawnRate = spawnRate;
    this.roadLength = roadLength;
  }
  
  public double getSpawnRate() {
    return this.spawnRate;
  }
  
  public Integer getRoadLength() {
    return this.roadLength;
  }
}
