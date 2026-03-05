package com.trafficsimulator.view;

import javafx.scene.layout.HBox;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;;

public class MenuView extends HBox {
  private Controller controller;
  private Button startSimButton;
  private Slider spawnRateControl;

  public MenuView(MainView mainView, Controller controller) {
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setSpacing(10);
    
    this.setupInputControls();
    
    this.startSimButton = new Button("Start");
    this.startSimButton.setOnAction(e -> {
      this.controller.setInputParameters(this.getInputValues());
      mainView.showSimulation();
    });
    this.getChildren().add(startSimButton);
  }
  
  private void setupInputControls() {
    Label spawnRateLabel = new Label("Spawn rate");
    this.spawnRateControl = new Slider(1, 10, 2);
    this.spawnRateControl.setSnapToTicks(true);
    this.spawnRateControl.setMajorTickUnit(1);

    this.getChildren().addAll(spawnRateLabel, spawnRateControl);
  }

  private InputParametersDTO getInputValues() {
    return new InputParametersDTO(
      (int)this.spawnRateControl.getValue()
    );
  }
}
