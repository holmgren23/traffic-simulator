package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.view.SubView;
import javafx.application.Platform;
import java.lang.Thread;

public class Controller {
  private final int TICK_LENGTH = 100;
  private TrafficSimulator ts;
  private SubView[] views;
  private long nextTick;

  public Controller(TrafficSimulator ts) {
    this.ts = ts;
    this.nextTick = System.currentTimeMillis() + TICK_LENGTH; 
  }
  
  public void setSubViews(SubView... subViews) {
    this.views = subViews;
  }
  
  public void loop() {
    while (true) {
      this.tick();
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
    Platform.runLater(() -> {
      this.updateViews();
    });
  }

}
