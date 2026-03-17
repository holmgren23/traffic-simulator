package com.trafficsimulator.view;

import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.controller.Controller;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class OfflineView extends VBox implements SubView {
  private Controller controller;
  private ProgressBar progressBar;
  private double progress, progressStep;

  public OfflineView(Controller controller, int ticksToRun) {
    this.controller = controller;
    this.progress = 0;
    this.progressStep = (double)Constants.PROGRESS_UPDATE_TICKS / ticksToRun;
    this.progressBar = new ProgressBar(this.progress);
    this.progressBar.setMaxWidth(Double.MAX_VALUE);

    Pane spacer = new Pane();
    spacer.setMinHeight(30);
    
    SimButton abortButton = new SimButton(
      "Cancel",
      "Stop simulation and show statistics summary [ESC]",
      e -> {
        this.controller.stopSimulation();
      }
    );
    abortButton.setPrefWidth(100);
    abortButton.setStyle(Constants.BTN_STYLE_RED);

    this.setAlignment(Pos.CENTER);
    
    this.getChildren().addAll(progressBar, spacer, abortButton);
  }
  
  public void update() {
    this.progress += this.progressStep;
    //System.out.println(this.progress);
    this.progressBar.setProgress(this.progress);
  }
}
