package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.view.SubView;

import javafx.application.Platform;

public class Controller {
  private final int TICK_LENGTH = 100;
  private TrafficSimulator ts;
  private TrafficSimulator ts2;
  private SubView[] views;
  private long nextTick;
  private boolean running;
  private boolean stopped;

  public Controller(TrafficSimulator ts, TrafficSimulator ts2) {
    this.ts = ts;
    this.ts2 = ts2;
    this.nextTick = System.currentTimeMillis() + TICK_LENGTH; 
  }
  
  public void setSubViews(SubView... subViews) {
    this.views = subViews;
  }
  
  public void loop() {
    this.stopped = false;
    this.running = true;

    while (! this.stopped) {
      if (this.running) {
        this.tick();
      }
      else {
        try {
          Thread.sleep(10);
        }
        catch (InterruptedException e) {
          System.err.println("Exception: " + e.getMessage());
          return;
        }
      }
    }
  }
  
  private void updateViews() {
    if (this.views.length <= 0) { return; }
    for (SubView sv : this.views) {
      sv.update();
    }
  }

  private void tick() {
    long currentTime;
    do {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        System.err.println("Exception: " + e.getMessage());
        return;
      }
      currentTime = System.currentTimeMillis();
    } while (currentTime < this.nextTick);

    this.nextTick = currentTime + TICK_LENGTH;
    this.ts.tickUpdate();
    this.ts2.tickUpdate();
    Platform.runLater(() -> {
      this.updateViews();
    });
  }

  public void toggleRunning() {
    this.running = this.running ? false : true;
  }
  
  public boolean isRunning() {
    return this.running;
  }
  
  public void stopSimulation() {
    this.stopped = true;
    Platform.exit();
  }
  
  public void resetSimulation() {
    this.running = false;

    try {
      Thread.sleep(15);
    } catch (InterruptedException e) {
      System.err.println("Exception: " + e.getMessage());
      return;
    }

    this.ts.resetSimulation();

    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      System.err.println("Exception: " + e.getMessage());
      return;
    }

    this.running = true;
  }
}
