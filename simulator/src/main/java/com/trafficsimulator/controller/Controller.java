package com.trafficsimulator.controller;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.utility.InputParametersDTO;
import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.utility.HelperMethods;
import com.trafficsimulator.view.MainView;
import com.trafficsimulator.view.SubView;

import javafx.application.Platform;

/**
 * The controller handles the mechanics of the program: switch between modes,
 * simulation loop, user input
 */
public class Controller {
  private double timeScaling = 1;
  private TrafficSimulator ts;
  private SubView[] views;
  private long nextTick;
  private boolean running;
  private boolean stopped;
  private int ticksTorun;
  private MainView mainView;
  
  /**
   * Creates a controller with a MainView object.
   * @param mainView Program's main view
   */
  public Controller(MainView mainView) {
    this.setNextTick();
    this.mainView = mainView;
  }

  /**
   * Creates a controller with a TrafficSimulator object. Kept for legacy reasons.
   * @param ts Predefined TrafficSimulator model
   */
  public Controller(TrafficSimulator ts) {
    this.ts = ts;
    this.setNextTick();
  }
  
  /**
   * Creates a TrafficSimulator model with the received user settings,
   * and start the simulation loop.
   * @param params Data transfer object containing user settings
   */
  public void createSimulation(InputParametersDTO params) {
    this.setTimeScaling(params.timeScalingFactor());

    ts = new TrafficSimulator(
      HelperMethods.percentToProbability((int)params.spawnPercent()),
      (int)params.roadLength(),
      (int)params.minSpeed(),
      (int)params.maxSpeed(),
      params.mapType()
    );
    
    MainView.setModel(ts);
    if (params.runOffline()) {
      this.ticksTorun = (int)params.ticksToRun();
      mainView.showOfflineRender(this.ticksTorun);
    }
    else {
      mainView.showSimulation(params.timeScalingRaw());
    }
  }
  
  /**
   * Sets views to be updated while running the simulation.
   * These views are used to show visuals of the simulation, among
   * other things.
   * @param subViews One or more views to be updated later by the loop
   */
  public void setSubViews(SubView... subViews) {
    this.views = subViews;
  }
  
  /**
   * Main loop that handles the time of the traffic simulation.
   * From here, all relevant parts of the simulation are updated.
   */
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
  
  /**
   * Loop for when running the simulation withou visuals.
   */
  public void loopOffline() {
    for (int i = 0; i < this.ticksTorun; i++) {
      ts.tickUpdate();
      if (i % Constants.PROGRESS_UPDATE_TICKS == 0) { this.updateViews(); }
    }
    Platform.runLater(() -> mainView.showSummaryScreen());
  }
  
  /**
   * Update views when a simulation is running.
   */
  private void updateViews() {
    if (this.views.length <= 0) { return; }
    for (SubView sv : this.views) {
      sv.update();
    }
  }

  /**
   * A tick, or step, of the simulation. Waits for set time, then updates
   * simulation (model) and views, in turn.
   */
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
  
  /**
   * Sets the time for when the next tick will happen. Used for
   * waiting between ticks.
   */
  private void setNextTick() {
    this.nextTick = System.currentTimeMillis() + (int)(Constants.TICK_LENGTH / this.timeScaling);
  }

  /**
   * Pauses/unpauses a running simulation.
   */
  public void toggleRunning() {
    this.running = this.running ? false : true;
  }
  
  /**
   * Returns true if the simulation is running and false if the simulation is paused.
   */
  public boolean isRunning() {
    return this.running;
  }
  
  /**
   * Stops a running simulation.
   */
  public void stopSimulation() {
    this.stopped = true;
  }
  
  /**
   * Clears simulation and statistics and restarts with the same user settings.
   */
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

  /**
   * Sets the time scaling for a running simulation.
   * @param timeScaling Expects a value greater than 0, where 100 is 1x speed
   */
  public void setTimeScaling(double timeScaling){
    this.timeScaling = (timeScaling / 100.0);
  }
}
