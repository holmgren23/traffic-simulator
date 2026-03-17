package com.trafficsimulator.view;

import com.trafficsimulator.utility.Constants;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class OfflineView extends VBox implements SubView {
  private ProgressBar progressBar;
  private double progress, progressStep;

  public OfflineView(int ticksToRun) {
    this.progress = 0;
    this.progressStep = (double)Constants.PROGRESS_UPDATE_TICKS / ticksToRun;
    this.progressBar = new ProgressBar(this.progress);
    this.progressBar.setMaxWidth(Double.MAX_VALUE);
    this.getChildren().add(progressBar);
  }
  
  public void update() {
    this.progress += this.progressStep;
    //System.out.println(this.progress);
    this.progressBar.setProgress(this.progress);
  }
}
