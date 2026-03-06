package com.trafficsimulator.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;

public class InputParametersDTOTest {
  private InputParametersDTO dto;

  @BeforeAll
  public void setUp() {
    dto = new InputParametersDTO(3);
  }
  
  @AfterAll
  public void cleanUp() {
    dto = null;
  }
  
  @Test
  public void testGetSpawnRate() {
    assertEquals(3, dto.getSpawnRate());
  }
}
