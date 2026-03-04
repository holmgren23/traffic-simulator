package com.trafficsimulator.controller;

import org.junit.jupiter.api.Test;
//import org.junit.jupiter.execution.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import com.trafficsimulator.model.TrafficSimulator;

public class ControllerTest {
  Controller controller;
  TrafficSimulator ts;
  Thread t;

  @BeforeEach
  public void setUp() {
    ts = new TrafficSimulator();
    controller = new Controller(ts);
    t = new Thread(() -> controller.loop());
  }
  
  @AfterEach
  public void cleanUp() {
    ts = null;
    controller = null;
    t = null;
  }
  
  @Test
  public void testStartAndStopSimulation() {
    int activeThreads = Thread.activeCount();
    t.start();
    assertEquals(activeThreads + 1, Thread.activeCount());
    controller.stopSimulation();
    try {
      t.join();
    }
    catch(Exception e) {
      System.out.println("Failed to join controller thread");
      System.out.println(e);
    }
    assertEquals(activeThreads, Thread.activeCount());
  }
  
  @Nested
  public class TestsWithMoreSetup {
    @BeforeEach
    public void setUp() {
      ts = new TrafficSimulator();
      controller = new Controller(ts);
      t = new Thread(() -> controller.loop());
      t.start();
    }
    
    @AfterEach
    public void cleanUp() throws InterruptedException {
      t.join();
      ts = null;
      controller = null;
      t = null;
    }

    @Test
    public void testIsRunning() {
      assertEquals(true, controller.isRunning());
      controller.stopSimulation();
    }
    
    @Test
    public void testToggleRunning() {
      assertEquals(true, controller.isRunning());
      controller.toggleRunning();
      assertEquals(false, controller.isRunning());
      controller.toggleRunning();
      assertEquals(true, controller.isRunning());
    }
  }
}