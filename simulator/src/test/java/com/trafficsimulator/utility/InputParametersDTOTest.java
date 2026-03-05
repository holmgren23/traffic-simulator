package com.trafficsimulator.utility;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputParametersDTOTest {
  private InputParametersDTO dto;

  @BeforeEach
  public void setUp() {
    dto = new InputParametersDTO(3);
  }
  
  @AfterEach
  public void cleanUp() {
    dto = null;
  }
  
  @Test
  public void testGetSpawnRate() {
    assertEquals(3, dto.getSpawnRate());
  }
}
