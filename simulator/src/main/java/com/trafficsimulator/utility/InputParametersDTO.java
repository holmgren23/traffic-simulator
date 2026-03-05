package com.trafficsimulator.utility;

public class InputParametersDTO {
  private int spawnRate;

  public InputParametersDTO(int spawnRate) {
    this.spawnRate = spawnRate;
  }
  
  public int getSpawnRate() {
    return this.spawnRate;
  }
}
