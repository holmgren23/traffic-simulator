package com.trafficsimulator.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.Constants;
import com.trafficsimulator.utility.HelperMethods;
import com.trafficsimulator.utility.InputParametersDTO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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
import javafx.scene.Node;

/**
 * The menu for user settings
 */
public class MenuView extends GridPane {
  private final int BTN_ROW = 7;
  private Controller controller;
  private SliderSection spawnRateSection, minSpeedSection, maxSpeedSection, timeScalingSection;
  private Spinner<Integer> roadLengthControl, ticksToRunControl;
  private ComboBox<String> mapSelector;
  private CheckBox offlineRunCheckBox;
  private int roadLengthMin = 5;
  private int roadLengthMax = 1200;
  private int roadLengthDefault = 15;
  private int offlineTimeMin = 30;
  private int offlineTimeMax = 500000;
  private int offlineTimeDefault = 600;
  private Tooltip minMaxAlert, maxMinAlert;
  
  /**
   * Defines styling, creates UI input elements and layout.
   * @param controller Program controller, to send setting values to
   */
  public MenuView(Controller controller) {
    this.controller = controller;

    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(28);
    this.setVgap(12);
    
    this.setupInputControls();
    this.setupNavBtns();
    this.restoreSettings();
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

    Button exitButton = new Button(Constants.BTN_TEXT_EXIT);
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
    this.setupMapSection();
    this.setupSpeedSection();
    this.setupMiscSection();
    this.setupOfflineSection();
  }
  
  private void setupMapSection() {
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
    
    Label roadLengthLabel = new Label("Road length (100 m)");
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(this.roadLengthMin, this.roadLengthMax, this.roadLengthDefault)
    );
    Tooltip roadLengthTooltip = new Tooltip("Set the length of the road (in 100 m increments)");
    roadLengthLabel.setTooltip(roadLengthTooltip);
    this.roadLengthControl.setTooltip(roadLengthTooltip);
    VBox roadLengthSection = new VBox(Constants.V_GAP, roadLengthLabel, roadLengthControl);
    
    Label[] labels = {
      mapSelectorLabel,
      roadLengthLabel
    };
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
    }

    // Add to leftmost column
    this.addColumn(0, 
      spacer(),
      mapSelectorSection,
      spacer(),
      roadLengthSection
    );
  }
  
  private void setupSpeedSection() {  
    Label idealSpeedLabel = new Label("Preferred car speed");
    idealSpeedLabel.setTooltip(new Tooltip("All cars in the simulation have a preferred speed, " +
      "randomized per car between the Min and Max values below."));
    minSpeedSection = new SliderSection("Min", " km/h", 1, 15, 2, v -> HelperMethods.speedToKMH(v), true);
    maxSpeedSection = new SliderSection("Max", " km/h", 1, 15, 6, v -> HelperMethods.speedToKMH(v), true);
    UnaryNumericOperation limitByMax = v -> {
      if (v <= maxSpeedSection.getValue()) {
        return Math.round(v);
      }
      else {
        this.showTooltipAlert(this.minMaxAlert, this.minSpeedSection);
        return maxSpeedSection.getSlider().getValue();
      }
    };
    UnaryNumericOperation limitByMin = v -> {
      if (v >= minSpeedSection.getValue()) {
        return Math.round(v);
      }
      else {
        this.showTooltipAlert(this.maxMinAlert, this.maxSpeedSection);
        return minSpeedSection.getSlider().getValue();
      }
    };
    minSpeedSection.setBehavior(limitByMax);
    maxSpeedSection.setBehavior(limitByMin);
    minSpeedSection.setTooltip("Set the minimum ideal speed for vehicles in the simulation");
    maxSpeedSection.setTooltip("Set the maximum ideal speed for vehicles in the simulation");

    minMaxAlert = new Tooltip("Min preferred speed must be lower than max preferred speed");
    maxMinAlert = new Tooltip("Max preferred speed must be higher than min preferred speed");
    
    idealSpeedLabel.setTextFill(Constants.TEXT_COLOR);
    
    Region bg = new Region();
    bg.setStyle(
      "-fx-background-color: rgb(54, 66, 56, 0.6);" +
      "-fx-background-radius: 15;"
    );
    
    // Add to middle column
    this.addColumn(1,
      idealSpeedLabel,
      minSpeedSection,
      spacer(),
      maxSpeedSection,
      spacer()
    );

    GridPane.setHalignment(idealSpeedLabel, HPos.CENTER);
    //minSpeedSection.setTranslateX(13);
    //maxSpeedSection.setTranslateX(13);

    this.add(bg, 1, 0, 1, 4);
    bg.toBack();
    GridPane.setHalignment(bg, HPos.CENTER);
    GridPane.setMargin(bg, new Insets(-8, -5, -8, -5));
    //bg.setTranslateX(-3);
    //bg.setTranslateY(-1);
  }

  private void setupMiscSection() {
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
    
    // Add to rightmost column
    this.addColumn(2,
      spacer(),
      spawnRateSection,
      spacer(),
      timeScalingSection
    );
  }
    
  private void setupOfflineSection() {
    Label offlineRunLabel = new Label("Run simulation\noffline (no visuals)");
    this.offlineRunCheckBox = new CheckBox();
    this.offlineRunCheckBox.setOnAction(e -> {
      this.ticksToRunControl.setDisable(! this.offlineRunCheckBox.isSelected());
      this.timeScalingSection.setDisable(this.offlineRunCheckBox.isSelected());
    });
    HBox offlineRunSection = new HBox(Constants.V_GAP, offlineRunLabel, offlineRunCheckBox);
    offlineRunSection.setAlignment(Pos.CENTER);
    
    Label ticksToRunLabel = new Label("Simulated runtime (min)");
    this.ticksToRunControl = new Spinner<Integer>();
    this.ticksToRunControl.setEditable(true);
    this.ticksToRunControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(this.offlineTimeMin, this.offlineTimeMax, this.offlineTimeDefault)
    );
    this.ticksToRunControl.setDisable(true);
    Tooltip ticksToRunTooltip = new Tooltip("Set the duration of the simulation in simulated minutes");
    ticksToRunLabel.setTooltip(ticksToRunTooltip);
    this.ticksToRunControl.setTooltip(ticksToRunTooltip);
    VBox ticksToRunSection = new VBox(Constants.V_GAP, ticksToRunLabel, ticksToRunControl);
    
    Label[] labels = {
      offlineRunLabel,
      ticksToRunLabel
    };
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
    }
    
    // Add to leftmost column
    this.addColumn(0, 
      spacer(),
      offlineRunSection
    );

    // Add to middle column
    this.addColumn(1,
      ticksToRunSection,
      spacer()
    );

    Region bg = new Region();
    bg.setStyle(
      "-fx-background-color: rgb(54, 66, 56, 0.6);" +
      "-fx-background-radius: 15;"
    );
    
    this.add(bg, 0, 5, 2, 1);
    bg.toBack();
    GridPane.setHalignment(bg, HPos.CENTER);
    GridPane.setMargin(bg, new Insets(-8, -10, -10, -10));
    bg.setTranslateX(5);
  }
  
  /**
   * Show extra tooltip at a parent Node location. Used to alert the user
   * that min/max speed is limited by each other.
   * @param tt Tooltip to show
   * @param parent Node whose position is where the tooltip is shown
   */
  private void showTooltipAlert(Tooltip tt, Node parent) {
    Bounds coords = parent.localToScreen(parent.getBoundsInLocal());
    tt.show(this, coords.getMinX(), coords.getMinY() - 35);
    CompletableFuture.delayedExecutor(1500, TimeUnit.MILLISECONDS)
      .execute(() -> Platform.runLater(() -> tt.hide()));
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
  
  /**
   * Gets previous user settings from the controller, and sets all
   * input controllers to match
   */
  private void restoreSettings() {
    InputParametersDTO userSettings = this.controller.getUserSettings();
    if (userSettings != null) {
      this.mapSelector.setValue(userSettings.mapType());
      this.roadLengthControl.setDisable(
        ! Constants.MAP_HAS_ADJUSTABLE_ROAD_LENGTH.get(this.mapSelector.getValue())
      );
      this.roadLengthControl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        this.roadLengthMin, this.roadLengthMax, userSettings.roadLength())
      );

      this.minSpeedSection.setValue(userSettings.minSpeed());
      this.maxSpeedSection.setValue(userSettings.maxSpeed());

      this.spawnRateSection.setValue(userSettings.spawnPercent());
      this.timeScalingSection.setDisable(userSettings.runOffline());
      this.timeScalingSection.setValue(userSettings.timeScalingRaw());

      this.offlineRunCheckBox.setSelected(userSettings.runOffline());
      this.ticksToRunControl.setDisable(! userSettings.runOffline());
      this.ticksToRunControl.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        this.offlineTimeMin, this.offlineTimeMax, (int)userSettings.ticksToRun())
      );
    }
  }
}
