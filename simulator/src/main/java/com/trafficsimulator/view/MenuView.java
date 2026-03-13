package com.trafficsimulator.view;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;
import com.trafficsimulator.utility.HelperMethods;
import com.trafficsimulator.utility.Constants;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MenuView extends GridPane {
  private final int BTN_ROW = 6;
  private MainView mainView;
  private Controller controller;
  private SliderSection spawnRateSection, minSpeedSection, maxSpeedSection, timeScalingSection;
  private Spinner<Integer> roadLengthControl, ticksToRunControl;
  private ComboBox<String> mapSelector;
  private CheckBox offlineRunCheckBox;
  
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
    });
    startSimButton.setTooltip(new Tooltip("Run the simulation"));
    this.add(startSimButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(startSimButton, HPos.LEFT);
    
    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());
    exitButton.setTooltip(new Tooltip("Quit the program and return to desktop"));
    this.add(exitButton, this.getColumnCount() - 1, this.BTN_ROW);
    GridPane.setHalignment(exitButton, HPos.RIGHT);
  }
  
  private void setupInputControls() {
    Label mapSelectorLabel = new Label("Select map");
    String[] maps = {"Highway", "One way street", "T Intersection"};
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(maps));
    if (maps.length > 0) { this.mapSelector.setValue(maps[0]); };
    Tooltip mapSelectorTooltip = new Tooltip("Select the type of map to simulate");
    mapSelectorLabel.setTooltip(mapSelectorTooltip);
    this.mapSelector.setTooltip(mapSelectorTooltip);
    VBox mapSelectorSection = new VBox(Constants.V_GAP, mapSelectorLabel, mapSelector);
    
    Label roadLengthLabel = new Label("Road length");
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200)
    );
    Tooltip roadLengthTooltip = new Tooltip("Set the length of the road (in number of cells)");
    roadLengthLabel.setTooltip(roadLengthTooltip);
    this.roadLengthControl.setTooltip(roadLengthTooltip);
    VBox roadLengthSection = new VBox(Constants.V_GAP, roadLengthLabel, roadLengthControl);
    
    minSpeedSection = new SliderSection("Min speed", " km/h", 1, 15, 2, v -> HelperMethods.speedToKMH(v), true);
    maxSpeedSection = new SliderSection("Max speed", " km/h", 1, 15, 6, v -> HelperMethods.speedToKMH(v), true);
    UnaryNumericOperation limitByMax = v -> v <= maxSpeedSection.getValue() ? Math.round(v) : maxSpeedSection.getSlider().getValue();
    UnaryNumericOperation limitByMin = v -> v >= minSpeedSection.getValue() ? Math.round(v) : minSpeedSection.getSlider().getValue();
    minSpeedSection.setBehavior(limitByMax);
    maxSpeedSection.setBehavior(limitByMin);
    minSpeedSection.setTooltip("Set the minimum ideal speed for vehicles in the simulation");
    maxSpeedSection.setTooltip("Set the maximum ideal speed for vehicles in the simulation");

    spawnRateSection = new SliderSection("Spawn rate", 1, Constants.MAX_TICKS_PER_SPAWN, 5);
    spawnRateSection.setBehavior(v ->  Math.round(v));
    spawnRateSection.setTooltip("Set how often a vehicle is spawned (in vehicles per second)");
    
    timeScalingSection = new SliderSection(
      "Time scaling",
      " %",
      0,
      1,
      ((Math.log(100) - Math.log(30)) / (Math.log(800) - Math.log(30))),
      v -> {
        double offset = Math.log(30);
        double scale = Math.log(800);
        return Math.round(Math.pow(Math.E, (offset + v * (scale - offset))));
      },
      false
    );
    timeScalingSection.setBehavior(v -> v);
    timeScalingSection.setTooltip("Set the playback speed of the simulation");
    
    Label offlineRunLabel = new Label("Run simulation\noffline (no visuals)");
    this.offlineRunCheckBox = new CheckBox();
    this.offlineRunCheckBox.setOnAction(e -> this.ticksToRunControl.setDisable(! this.offlineRunCheckBox.isSelected()));
    HBox offlineRunSection = new HBox(Constants.V_GAP, offlineRunLabel, offlineRunCheckBox);
    offlineRunSection.setAlignment(Pos.CENTER);
    
    Label ticksToRunLabel = new Label("Number of ticks to run");
    this.ticksToRunControl = new Spinner<Integer>();
    this.ticksToRunControl.setEditable(true);
    this.ticksToRunControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 10000000, 10000)
    );
    this.ticksToRunControl.setDisable(true);
    Tooltip ticksToRunTooltip = new Tooltip("Set the length of the road (in number of cells)");
    ticksToRunLabel.setTooltip(ticksToRunTooltip);
    this.ticksToRunControl.setTooltip(ticksToRunTooltip);
    VBox ticksToRunSection = new VBox(Constants.V_GAP, ticksToRunLabel, ticksToRunControl);
    
    Label[] labels = {
      mapSelectorLabel,
      roadLengthLabel,
      offlineRunLabel,
      ticksToRunLabel
    };
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
    }
    
    // Columns from the left
    this.addColumn(0, 
      mapSelectorSection,
      spacer(),
      roadLengthSection,
      spacer(),
      offlineRunSection
    );
    
    this.addColumn(1,
      minSpeedSection,
      spacer(),
      maxSpeedSection,
      spacer(),
      ticksToRunSection
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
      this.timeScalingSection.getValue(),
      this.timeScalingSection.getSlider().getValue(),
      this.offlineRunCheckBox.isSelected(),
      this.ticksToRunControl.getValue()
    );
  }
}
