package com.trafficsimulator.controller;

import org.junit.jupiter.api.Test;
//import org.junit.jupiter.execution.Timeout;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.view.RoadView;

public class ControllerTest {
  Controller controller;
  TrafficSimulator ts;
  RoadView rv;

  @BeforeAll
  public void setUp() {
    ts = new TrafficSimulator();
    rv = new RoadView(ts, 1280);
    controller = new Controller(ts);
    controller.setRoadView(rv);
  }
  
  @AfterAll
  public void cleanUp() {
    ts = null;
    rv = null;
    controller = null;
  }

  @Test
  public void testTick() {
    long start = System.currentTimeMillis();
    //controller.tick();
  }

  @Test
  //@Timeout(5) // Poll at most 5 seconds
  void pollUntil() throws InterruptedException {
  	//while (asynchronousResultNotAvailable()) {
  		//Thread.sleep(250); // custom poll interval
  	//}
  	// Obtain the asynchronous result and perform assertions
  }
}
