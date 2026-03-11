package com.trafficsimulator.view;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;
import com.trafficsimulator.utility.Constants;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MenuView extends GridPane {
  private final int BTN_ROW = 6;
  private MainView mainView;
  private Controller controller;
  private SliderSection spawnRateSection, minSpeedSection, maxSpeedSection, timeScalingSection;
  private Spinner<Integer> roadLengthControl;
  private ComboBox<String> mapSelector;
  
  public MenuView(MainView mainView, Controller controller) {
    this.mainView = mainView;
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(25);
    this.setVgap(10);
    
    this.setupInputControls();
    this.setupNavBtns();
  }
  
  private Pane spacer() {
    Pane spacer = new Pane();
    spacer.setMinHeight(25);
    return spacer;
  }
  
  private void setupNavBtns() {
    Button startSimButton = new Button("Start");
    startSimButton.setOnAction(e -> {
      this.controller.createSimulation(this.getInputValues());
      this.mainView.showSimulation((int)this.timeScalingSection.getValue());
    });
    this.add(startSimButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(startSimButton, HPos.LEFT);
    
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());
    this.add(exitButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(exitButton, HPos.RIGHT);
  }
  
  private void setupInputControls() {
    Label mapSelectorLabel = new Label("Select map");
    String[] maps = {"Highway", "One way street", "T Intersection"};
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(maps));
    if (maps.length > 0) { this.mapSelector.setValue(maps[0]); };
    VBox mapSelectorSection = new VBox(Constants.V_GAP, mapSelectorLabel, mapSelector);
    
    Label roadLengthLabel = new Label("Road length");
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200)
    );
    VBox roadLengthSection = new VBox(Constants.V_GAP, roadLengthLabel, roadLengthControl);
    
    minSpeedSection = new SliderSection("Min speed", 1, 15, 2);
    maxSpeedSection = new SliderSection("Max speed", 1, 15, 6);
    FindInputValue limitByMax = v -> v <= maxSpeedSection.getSlider().getValue() ? Math.round(v) : maxSpeedSection.getSlider().getValue();
    FindInputValue limitByMin = v -> v >= minSpeedSection.getSlider().getValue() ? Math.round(v) : minSpeedSection.getSlider().getValue();
    minSpeedSection.setBehavior(limitByMax);
    maxSpeedSection.setBehavior(limitByMin);

    spawnRateSection = new SliderSection("Spawn interval", 1, 10, 5);
    FindInputValue returnNewValue = v ->  Math.round(v);
    spawnRateSection.setBehavior(returnNewValue);
    
    timeScalingSection = new SliderSection("Time scaling", " %", 20, 800, 100);
    timeScalingSection.setBehavior(returnNewValue);
    
    Label[] labels = {
      mapSelectorLabel,
      roadLengthLabel
    };
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
    }
    
    // Columns from the left
    this.addColumn(0, 
      mapSelectorSection,
      spacer(),
      roadLengthSection
    );
    
    this.addColumn(1,
      minSpeedSection,
      spacer(),
      maxSpeedSection
    );
    
    this.addColumn(2,
      spawnRateSection,
      spacer(),
      timeScalingSection
    );
  }

  private InputParametersDTO getInputValues() {
    return new InputParametersDTO(
      this.mapSelector.getValue(),
      this.roadLengthControl.getValue(),
      this.minSpeedSection.getValue(),
      this.maxSpeedSection.getValue(),
      this.spawnRateSection.getValue(),
      this.timeScalingSection.getValue()
    );
  }
}
