package com.trafficsimulator.view;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.Desktop;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.utility.HelperMethods;
import com.trafficsimulator.utility.InputParametersDTO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * The menu for user settings
 */
public class MenuView extends GridPane {
  private final int BTN_ROW = 6;
  private Controller controller;
  private SliderSection spawnRateSection, minSpeedSection, maxSpeedSection, timeScalingSection;
  private Spinner<Integer> roadLengthControl, ticksToRunControl;
  private ComboBox<String> mapSelector;
  private CheckBox offlineRunCheckBox;
  
  /**
   * Defines styling, creates UI input elements and layout.
   * @param controller Program controller, to send setting values to
   */
  public MenuView(Controller controller) {
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(25);
    this.setVgap(10);
    
    this.setupInputControls();
    this.setupNavBtns();
  }
  
  /**
   * An empty UI element to add space to the menu layout.
   * @return An empty Pane element with a fixed height
   */
  private Pane spacer() {
    Pane spacer = new Pane();
    spacer.setMinHeight(25);
    return spacer;
  }
  
  /**
   * Defines and creates buttons: start and exit.
   * Start runs a new simulation with current settings.
   * Exit quits to desktop.
   */
  private void setupNavBtns() {
    Button startSimButton = new Button("Start");
    startSimButton.setOnAction(e -> {
      this.controller.createSimulation(this.getInputValues());
    });
    startSimButton.setTooltip(new Tooltip("Run the simulation"));
    startSimButton.setPrefWidth(Constants.BTN_WIDTH);
    startSimButton.setStyle(Constants.BTN_STYLE_GREEN);

    Button exitButton = new Button("Exit");
    exitButton.setOnAction(e -> Platform.exit());
    exitButton.setTooltip(new Tooltip("Quit the program and return to desktop"));
    exitButton.setPrefWidth(Constants.BTN_WIDTH);
    exitButton.setStyle(Constants.BTN_STYLE_RED);
    
    Button helpButton = new Button("Help");
    helpButton.setOnAction(e -> new Thread( () -> {
        try {
          File pdfFile = new File("user manual/user_manual.pdf");
          Desktop.getDesktop().open(pdfFile);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }).start()
    );
    helpButton.setTooltip(new Tooltip("Open the user manual PDF file"));
    helpButton.setPrefWidth(Constants.BTN_WIDTH);
    helpButton.setStyle(Constants.BTN_STYLE_BLUE);

    HBox btnRow = new HBox(20, startSimButton, helpButton, exitButton);
    btnRow.setAlignment(Pos.CENTER);
    this.add(btnRow, 0, this.BTN_ROW, this.getColumnCount(), 1);
  }
  
  /**
   * Defines and creates UI elements for user settings,
   * and a layout for these.
   */
  private void setupInputControls() {
    Label mapSelectorLabel = new Label("Select map");
    String[] maps = Arrays.copyOf(
      Constants.MAP_HAS_ADJUSTABLE_ROAD_LENGTH.keySet().toArray(),
      Constants.MAP_HAS_ADJUSTABLE_ROAD_LENGTH.keySet().size(),
      String[].class
    );
    Arrays.sort(maps);
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(maps));
    if (maps.length > 0) { this.mapSelector.setValue(maps[0]); };
    Tooltip mapSelectorTooltip = new Tooltip("Select the type of map to simulate");
    mapSelectorLabel.setTooltip(mapSelectorTooltip);
    this.mapSelector.setTooltip(mapSelectorTooltip);
    this.mapSelector.setOnAction(e -> this.roadLengthControl.setDisable(
      ! Constants.MAP_HAS_ADJUSTABLE_ROAD_LENGTH.get(this.mapSelector.getValue())
    ));
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
    
    Label idealSpeedLabel = new Label("Preferred car speed");
    idealSpeedLabel.setTooltip(new Tooltip("All cars in the simulation have a preferred speed, " +
      "randomized per car between the Min and Max values below."));
    minSpeedSection = new SliderSection("Min", " km/h", 1, 15, 2, v -> HelperMethods.speedToKMH(v), true);
    maxSpeedSection = new SliderSection("Max", " km/h", 1, 15, 6, v -> HelperMethods.speedToKMH(v), true);
    UnaryNumericOperation limitByMax = v -> v <= maxSpeedSection.getValue() ? Math.round(v) : maxSpeedSection.getSlider().getValue();
    UnaryNumericOperation limitByMin = v -> v >= minSpeedSection.getValue() ? Math.round(v) : minSpeedSection.getSlider().getValue();
    minSpeedSection.setBehavior(limitByMax);
    maxSpeedSection.setBehavior(limitByMin);
    minSpeedSection.setTooltip("Set the minimum ideal speed for vehicles in the simulation");
    maxSpeedSection.setTooltip("Set the maximum ideal speed for vehicles in the simulation");
    Region spacer16 = new Region();
    Region spacer17 = new Region();
    spacer16.setPrefHeight(16);
    spacer17.setPrefHeight(18);
    VBox speedColumn = new VBox(0, idealSpeedLabel, spacer16, minSpeedSection, spacer17, maxSpeedSection);

    spawnRateSection = new SliderSection(
      "Spawn chance",
      " %",
      Constants.SPAWN_PROBABILITY_MIN,
      Constants.SPAWN_PROBABILITY_MAX,
      Constants.SPAWN_PROBABILITY_DEFATULT
    );
    spawnRateSection.setBehavior(v ->  Math.round(v));
    spawnRateSection.setTooltip("The likelihood for a car spawning each tick, in each spawning location");
    
    timeScalingSection = new SliderSection(
      "Time scaling",
      " %",
      0,
      1,
      ((Math.log(Constants.TIME_SCALING_DEFAULT_PERCENTAGE) - Math.log(Constants.TIME_SCALING_MIN_PERCENTAGE)) /
      (Math.log(Constants.TIME_SCALING_MAX_PERCENTAGE) - Math.log(Constants.TIME_SCALING_MIN_PERCENTAGE))),
      v -> {
        double offset = Math.log(Constants.TIME_SCALING_MIN_PERCENTAGE);
        double scale = Math.log(Constants.TIME_SCALING_MAX_PERCENTAGE);
        return Math.round(Math.pow(Math.E, (offset + v * (scale - offset))));
      },
      false
    );
    timeScalingSection.setBehavior(v -> v);
    timeScalingSection.setTooltip("Set the playback speed of the simulation");
    
    Label offlineRunLabel = new Label("Run simulation\noffline (no visuals)");
    this.offlineRunCheckBox = new CheckBox();
    this.offlineRunCheckBox.setOnAction(e -> {
      this.ticksToRunControl.setDisable(! this.offlineRunCheckBox.isSelected());
      this.timeScalingSection.setDisable(this.offlineRunCheckBox.isSelected());
    });
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
      idealSpeedLabel,
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
    
    this.add(speedColumn, 1, 0, 1, 3);
    this.addColumn(1,
      spacer(),
      ticksToRunSection
    );
    
    this.addColumn(2,
      spawnRateSection,
      spacer(),
      timeScalingSection
    );
  }

  /**
   * Collect user settings into a data transfer object.
   * @return Data transfer object (DTO) with user set values.
   */
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
