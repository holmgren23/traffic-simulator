package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.view.MainView;

import java.lang.Thread;

public class Controller {
  private final int TICK_LENGTH = 20;
  private TrafficSimulator ts;
  private long nextTick;

  public Controller(TrafficSimulator ts) {
    this.ts = ts;
    this.nextTick = System.currentTimeMillis() + TICK_LENGTH;
  }
  
  public void loop() {
    while (true) {
      this.tick();
    }
  }
  
  private void tick() {
    long currentTime;
    do {
      try {
        Thread.sleep(10);
      }
      catch (InterruptedException e) {
        System.err.println("Exception: " + e.getMessage());
        return;
      }
      currentTime = System.currentTimeMillis();
    } while (currentTime < this.nextTick);
    
    this.nextTick = currentTime + TICK_LENGTH;
    this.ts.tickUpdate();
  }

}
