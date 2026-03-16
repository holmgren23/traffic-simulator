package com.trafficsimulator.controller;

import org.junit.jupiter.api.Test;
//import org.junit.jupiter.execution.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import com.trafficsimulator.model.Map;
import com.trafficsimulator.model.MapType;
import com.trafficsimulator.model.Road;

import java.util.ArrayList;
import java.util.Random;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.model.Road.Direction;
import com.trafficsimulator.model.Car;

public class ControllerTest {
  Controller controller;
  TrafficSimulator ts;
  Thread t;
  Map map;

  @BeforeEach
  public void setUp() {
    map = new Map(MapType.ONE_WAY);
    map.addRoad(new Road(200, Direction.EAST, 0, 0, 100, 100, 5));
    ts = new TrafficSimulator(new Random(), map);
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
      Map map = new Map(MapType.ONE_WAY);
      map.addRoad(new Road(200, Direction.EAST, 0, 0, 100, 100, 5));
      ts = new TrafficSimulator(new Random(), map);
      controller = new Controller(ts);
      t = new Thread(() -> controller.loop());
      t.start();
      try { Thread.sleep(50); } //Give loop() Time to run
      catch (InterruptedException e) { }
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
    
    @Test
    public void testResetSimulationRunning() {
      ArrayList<Car> cars = ts.getCars();
      controller.resetSimulation();
      assertTrue(controller.isRunning());
      assertNotSame(cars, ts.getCars());
    }

    @Test
    public void testResetSimulationNotRunning() {
      controller.toggleRunning();
      ArrayList<Car> cars = ts.getCars();
      controller.resetSimulation();
      assertTrue(controller.isRunning());
      assertNotSame(cars, ts.getCars());
    }
  }
}