package com.trafficsimulator.view;

import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.controller.Controller;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A view showing a progress bar for when running simulations
 * without graphics/visuals (offline mode). Also has a
 * Cancel button.
 */
public class OfflineView extends VBox implements SubView {
  private Controller controller;
  private ProgressBar progressBar;
  private double progress, progressStep;

  /**
   * Creates a view suitable for running simulations without 
   * graphics (offline mode). Shows a progress bar and a Cancel
   * button.
   * @param controller Controller that runs the simulation loop, to be
   * able to abort/cancel simulation when user presses the Cancel button.
   * @param ticksToRun Number of ticks the simulation will run for. Used
   * to calculate progress bar increments.
   */
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
  
  /**
   * Update the progress bar by one step.
   * One step is calculated by a constant (defaults to 1000 ticks)
   * divided by the simulation duration in number of ticks.
   */
  public void update() {
    this.progress += this.progressStep;
    //System.out.println(this.progress);
    this.progressBar.setProgress(this.progress);
  }
}
