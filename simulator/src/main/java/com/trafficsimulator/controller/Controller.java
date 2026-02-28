package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.view.RoadView;
import com.trafficsimulator.view.StatisticsView;
import javafx.application.Platform;
import java.lang.Thread;

public class Controller {
  private final int TICK_LENGTH = 100;
  private TrafficSimulator ts;
  private RoadView roadView;
  private StatisticsView statsView;
  private long nextTick;

  public Controller(TrafficSimulator ts, RoadView roadView, StatisticsView statsView) {
    this.ts = ts;
    this.roadView = roadView;
    this.statsView = statsView;
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
      } catch (InterruptedException e) {
        System.err.println("Exception: " + e.getMessage());
        return;
      }
      currentTime = System.currentTimeMillis();
    } while (currentTime < this.nextTick);

    this.nextTick = currentTime + TICK_LENGTH;
    this.ts.tickUpdate();
    Platform.runLater(() -> {
    roadView.render();
    statsView.update();
    });
  }

}
