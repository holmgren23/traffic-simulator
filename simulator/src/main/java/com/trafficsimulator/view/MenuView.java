package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ComboBox;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;;

public class MenuView extends GridPane {
  private Controller controller;
  private Button startSimButton;
  private Slider spawnRateControl, minSpeedControl, maxSpeedControl;
  private Spinner<Integer> roadLengthControl;
  private ComboBox<String> mapSelector;

  public MenuView(MainView mainView, Controller controller) {
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(20);
    this.setVgap(10);
    
    this.setupInputControls();
    
    this.startSimButton = new Button("Start");
    this.startSimButton.setOnAction(e -> {
      this.controller.createSimulation(this.getInputValues());
      mainView.showSimulation();
    });
    this.add(startSimButton, this.getColumnCount() - 1, 6);
    GridPane.setHalignment(startSimButton, HPos.RIGHT);
  }
  
  private Pane spacer() {
    Pane spacer = new Pane();
    spacer.setMinHeight(40);
    return spacer;
  }
  
  private void setupInputControls() {
    Label spawnRateLabel = new Label("Spawn interval");
    spawnRateLabel.setTextFill(Color.SILVER);
    this.spawnRateControl = new Slider(1, 10, 5);
    this.spawnRateControl.setSnapToTicks(true);
    this.spawnRateControl.setMajorTickUnit(1);
    
    Label roadLengthLabel = new Label("Road length");
    spawnRateLabel.setTextFill(Color.SILVER);
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200));
    
    Label minSpeedLabel = new Label("Min speed");
    this.minSpeedControl = new Slider(1, 15, 2);
    this.minSpeedControl.setSnapToTicks(true);
    this.minSpeedControl.setMajorTickUnit(1);

    Label maxSpeedLabel = new Label("Max speed");
    this.maxSpeedControl = new Slider(1, 15, 5);
    this.maxSpeedControl.setSnapToTicks(true);
    this.maxSpeedControl.setMajorTickUnit(1);

    Label mapSelectorLabel = new Label("Select map");
    String[] dummyMaps = {"Dummy1", "Dummy2", "Dummy3"};
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(dummyMaps));
    
    Label[] labels = {spawnRateLabel, roadLengthLabel, minSpeedLabel, maxSpeedLabel, mapSelectorLabel};
    for (int i = 0; i < labels.length; i++) {
      labels[i].setTextFill(Color.SILVER);
    }

    // Columns from the left
    this.addColumn(0, 
      spawnRateLabel, this.spawnRateControl,
      spacer(),
      roadLengthLabel, this.roadLengthControl
    );
    
    this.addColumn(1,
      minSpeedLabel, this.minSpeedControl,
      spacer(),
      maxSpeedLabel, this.maxSpeedControl
    );
    
    this.addColumn(2,
      mapSelectorLabel, this.mapSelector
    );
  }

  private InputParametersDTO getInputValues() {
    return new InputParametersDTO(
      this.spawnRateControl.getValue(),
      this.roadLengthControl.getValue(),
      this.minSpeedControl.getValue(),
      this.maxSpeedControl.getValue()
    );
  }
}
