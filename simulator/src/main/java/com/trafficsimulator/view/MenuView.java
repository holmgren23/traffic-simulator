package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;;

public class MenuView extends GridPane {
  private final int NO_ROWS = 2;
  
  private Controller controller;
  private Button startSimButton;
  private Slider spawnRateControl;
  private Spinner<Integer> roadLengthControl;

  public MenuView(MainView mainView, Controller controller) {
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    //this.setSpacing(10);
    
    for (int i = 0; i < this.NO_ROWS; i++) {
      this.addRow(i);
    }
    
    this.setupInputControls();
    
    this.startSimButton = new Button("Start");
    this.startSimButton.setOnAction(e -> {
      this.controller.setInputParameters(this.getInputValues());
      mainView.showSimulation();
    });
    //this.getChildren().add(startSimButton);
    this.addColumn(4, startSimButton);
  }
  
  private Pane spacer() {
    Pane spacer = new Pane();
    spacer.setMinHeight(40);
    return spacer;
  }
  
  private void setupInputControls() {
    Label spawnRateLabel = new Label("Spawn interval");
    this.spawnRateControl = new Slider(1, 10, 5);
    this.spawnRateControl.setSnapToTicks(true);
    this.spawnRateControl.setMajorTickUnit(1);
    
    Label roadLengthLabel = new Label("Road length");
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200));

    this.addColumn(1, 
      spawnRateLabel, spawnRateControl,
      spacer(),
      roadLengthLabel, roadLengthControl
    );
  }

  private InputParametersDTO getInputValues() {
    return new InputParametersDTO(
      this.spawnRateControl.getValue(),
      this.roadLengthControl.getValue()
    );
  }
}
