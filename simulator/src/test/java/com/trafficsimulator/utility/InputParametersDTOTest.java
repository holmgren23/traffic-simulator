package com.trafficsimulator.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputParametersDTOTest {
  private InputParametersDTO dto;

  @BeforeEach
  public void setUp() {
    dto = new InputParametersDTO(2, 3, 4, 5, "testMap");
  }
  
  @AfterEach
  public void cleanUp() {
    dto = null;
  }
  
  @Test
  public void testGetSpawnRate() {
    assertEquals(2, dto.getSpawnRate());
  }
  
  @Test
  public void testGetRoadLength() {
    assertEquals(3, dto.getRoadLength());
  }
   @Test
   public void testGetMinSpeed() {
    assertEquals(4, dto.getMinSpeed());
   }

   @Test
   public void testGetMaxSpeed() {
    assertEquals(5, dto.getMaxSpeed());
   }
   
   @Test
   public void testGetMapType() {
    assertEquals("testMap", dto.getMapType());
   }
}
