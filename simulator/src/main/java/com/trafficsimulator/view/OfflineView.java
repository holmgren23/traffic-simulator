package com.trafficsimulator.view;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class OfflineView extends VBox {
  private ProgressBar progressBar;

  public OfflineView() {
    this.progressBar = new ProgressBar();
    this.getChildren().add(progressBar);
  }
}
