package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.utility.InputParametersDTO;
import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.view.MainView;
import com.trafficsimulator.view.SubView;
import com.trafficsimulator.model.Map;

import javafx.application.Platform;

public class Controller {
  private double timeScaling = 1;
  private TrafficSimulator ts;
  private SubView[] views;
  private long nextTick;
  private boolean running;
  private boolean stopped;
  
  public Controller() {
    this.setNextTick();
  }

  public Controller(TrafficSimulator ts) {
    this.ts = ts;
    this.setNextTick();
  }
  
  public void createSimulation(InputParametersDTO params) {
    this.setTimeScaling((int)params.getTimeScalingFactor());

    Map selctedMap = switch (params.getMapType()){
      case "Highway" -> Map.createHighway(params.getRoadLength());
      case "One way street" -> Map.createOneWay(params.getRoadLength());
      case "T Intersection" -> Map.createTIntersection(params.getRoadLength());
      default -> Map.createHighway(params.getRoadLength()); 
    };

    ts = new TrafficSimulator(
      (int)params.getSpawnRate(),
      (int)params.getRoadLength(),
      (int)params.getMinSpeed(),
      (int)params.getMaxSpeed(),
      selctedMap
    );

    MainView.setModel(ts);
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

    this.setNextTick();
    this.ts.tickUpdate();
    Platform.runLater(() -> {
      this.updateViews();
    });
  }
  
  private void setNextTick() {
    this.nextTick = System.currentTimeMillis() + (int)(Constants.TICK_LENGTH / this.timeScaling);
  }

  public void toggleRunning() {
    this.running = this.running ? false : true;
  }
  
  public boolean isRunning() {
    return this.running;
  }
  
  public void stopSimulation() {
    this.stopped = true;
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

  public void setTimeScaling(int timeScaling){
    this.timeScaling = timeScaling / 100.0;
  }
}
