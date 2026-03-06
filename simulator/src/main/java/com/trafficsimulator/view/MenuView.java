package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
  private final int BTN_ROW = 6;
  private MainView mainView;
  private Controller controller;
  private Slider spawnRateControl, minSpeedControl, maxSpeedControl;
  private Spinner<Integer> roadLengthControl;
  private ComboBox<String> mapSelector;

  public MenuView(MainView mainView, Controller controller) {
    this.mainView = mainView;
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(20);
    this.setVgap(10);
    
    this.setupInputControls();
    this.setupNavBtns();
  }
  
  private Pane spacer() {
    Pane spacer = new Pane();
    spacer.setMinHeight(40);
    return spacer;
  }
  
  private void setupNavBtns() {
    Button startSimButton = new Button("Start");
    startSimButton.setOnAction(e -> {
      this.controller.createSimulation(this.getInputValues());
      this.mainView.showSimulation();
    });
    this.add(startSimButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(startSimButton, HPos.LEFT);
    
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());
    this.add(exitButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(exitButton, HPos.RIGHT);
  }
  
  private void setupInputControls() {
    Label spawnRateLabel = new Label("Spawn interval");
    Label spawnRateValueLabel = new Label("");
    spawnRateLabel.setTextFill(Color.SILVER);
    this.spawnRateControl = new Slider(1, 10, 5);
    this.spawnRateControl.valueProperty().addListener((obs, oldVal, newVal) -> {
      double rounded = Math.round(newVal.doubleValue());
      this.spawnRateControl.setValue(rounded);
      spawnRateValueLabel.setText(String.valueOf((int)rounded));
    });
    
    Label roadLengthLabel = new Label("Road length");
    spawnRateLabel.setTextFill(Color.SILVER);
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200)
    );
    
    Label minSpeedLabel = new Label("Min speed");
    Label minSpeedValueLabel = new Label("");
    this.minSpeedControl = new Slider(1, 15, 2);
    this.minSpeedControl.valueProperty().addListener((obs, oldVal, newVal) -> {
      double nv = newVal.doubleValue();
      double value = nv <= this.maxSpeedControl.getValue() ? nv : oldVal.doubleValue();
      this.minSpeedControl.setValue(Math.round(value));
      minSpeedValueLabel.setText(String.valueOf((int)value));
    });

    Label maxSpeedLabel = new Label("Max speed");
    Label maxSpeedValueLabel = new Label("");
    this.maxSpeedControl = new Slider(1, 15, 5);
    this.maxSpeedControl.valueProperty().addListener((obs, oldVal, newVal) -> {
      double value = newVal.doubleValue() >= this.minSpeedControl.getValue() ? newVal.doubleValue() : oldVal.doubleValue();
      this.maxSpeedControl.setValue(Math.round(value));
      maxSpeedValueLabel.setText(String.valueOf((int)value));
    });


    Label mapSelectorLabel = new Label("Select map");
    String[] dummyMaps = {"Dummy1", "Dummy2", "Dummy3"};
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(dummyMaps));
    
    Label[] labels = {
      spawnRateLabel,
      spawnRateValueLabel,
      roadLengthLabel,
      minSpeedLabel,
      minSpeedValueLabel,
      maxSpeedLabel,
      maxSpeedValueLabel,
      mapSelectorLabel
    };
    for (Label l : labels) {
      l.setTextFill(Color.SILVER);
    }
    
    Slider[] sliders = {spawnRateControl, minSpeedControl, maxSpeedControl};
    for (Slider s : sliders) {
      s.setMajorTickUnit(1);
      s.setShowTickLabels(true);
    }

    // Columns from the left
    this.addColumn(0, 
      spawnRateLabel, this.spawnRateControl,
      spawnRateValueLabel,
      roadLengthLabel, this.roadLengthControl
    );
    
    this.addColumn(1,
      minSpeedLabel, this.minSpeedControl,
      minSpeedValueLabel,
      maxSpeedLabel, this.maxSpeedControl,
      maxSpeedValueLabel
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
