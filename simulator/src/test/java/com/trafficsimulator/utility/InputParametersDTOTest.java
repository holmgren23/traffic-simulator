package com.trafficsimulator.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputParametersDTOTest {
  private InputParametersDTO dto;

  @BeforeEach
  public void setUp() {
    dto = new InputParametersDTO(
      "testMap",
      3,
      4,
      5,
      6,
      7,
      0.2,
      true,
      50000
    );
  }
  
  @AfterEach
  public void cleanUp() {
    dto = null;
  }
  
  @Test
  public void testGetMapType() {
   assertEquals("testMap", dto.mapType());
  }
  
  @Test
  public void testGetRoadLength() {
    assertEquals(3, dto.roadLength());
  }

  @Test
  public void testGetMinSpeed() {
   assertEquals(4, dto.minSpeed());
  }

  @Test
  public void testGetMaxSpeed() {
   assertEquals(5, dto.maxSpeed());
  }
   
  @Test
  public void testGetSpawnPercent() {
    assertEquals(6, dto.spawnPercent());
  }
  
  @Test
  public void testGetTimeScaling() {
    assertEquals(7, dto.timeScalingFactor());
  }
  
  @Test
  public void testTimeScalingRaw() {
    assertEquals(0.2, dto.timeScalingRaw());
  }
  
  @Test
  public void testRunOffline() {
    assertTrue(dto.runOffline());
  }
  
  @Test
  public void testTicksToRund() {
    assertEquals(50000, dto.ticksToRun());
  }
}
