package com.trafficsimulator.view;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.InputParametersDTO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;;

public class MenuView extends GridPane {
  private final int BTN_ROW = 6;
  private MainView mainView;
  private Controller controller;
  private SliderSection spawnRateSection, minSpeedSection, maxSpeedSection;
  private Spinner<Integer> roadLengthControl;
  private ComboBox<String> mapSelector;
  private Color textColor = Color.SILVER;
  private int vGap = 12;
  
  @FunctionalInterface
  interface FindInputValue {
    double operate(double v);
  }
  
  private class SliderSection extends GridPane {
    private Slider inputSlider;
    private Label valueLabel;

    public SliderSection(String labelText, int min, int max, int initVal) {
      this.setStyle("-fx-padding: 2;");
      this.setHgap(0);
      this.setVgap(vGap);

      Label label = new Label(labelText + ": ");
      this.valueLabel = new Label(String.valueOf(initVal));
      this.inputSlider = new Slider(min, max, initVal);
      
      Label[] labels = {label, valueLabel};
      for (Label l : labels) {
        l.setTextFill(textColor);
        this.add(l, 0, 0);
      }
      this.add(inputSlider, 0, 1);

      GridPane.setHalignment(valueLabel, HPos.RIGHT);
      //GridPane.setHgrow(inputSlider, Priority.ALWAYS);
    }
    
    public void setBehavior(FindInputValue expr) {
      this.inputSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
        double rounded = expr.operate(newVal.doubleValue());
        inputSlider.setValue(rounded);
        valueLabel.setText(String.valueOf((int)rounded));
      });
    }
    
    public Slider getSlider() {
      return this.inputSlider;
    }
    
    public double getValue() {
      return this.inputSlider.getValue();
    }
  }

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
    spawnRateSection = new SliderSection("Spawn interval", 1, 10, 5);
    FindInputValue returnNewValue = v ->  Math.round(v);
    spawnRateSection.setBehavior(returnNewValue);
    
    Label roadLengthLabel = new Label("Road length");
    this.roadLengthControl = new Spinner<Integer>();
    this.roadLengthControl.setEditable(true);
    this.roadLengthControl.setValueFactory(
      new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 10000, 200)
    );
    VBox roadLengthSection = new VBox(vGap, roadLengthLabel, roadLengthControl);
    
    minSpeedSection = new SliderSection("Min speed", 1, 15, 2);
    maxSpeedSection = new SliderSection("Max speed", 1, 15, 6);
    FindInputValue limitByMax = v -> v <= maxSpeedSection.getSlider().getValue() ? Math.round(v) : maxSpeedSection.getSlider().getValue();
    FindInputValue limitByMin = v -> v >= minSpeedSection.getSlider().getValue() ? Math.round(v) : minSpeedSection.getSlider().getValue();
    minSpeedSection.setBehavior(limitByMax);
    maxSpeedSection.setBehavior(limitByMin);

    Label mapSelectorLabel = new Label("Select map");
    String[] maps = {"Highway", "One way street", "T Intersection"};
    this.mapSelector = new ComboBox<String>(FXCollections.observableArrayList(maps));
    if (maps.length > 0) { this.mapSelector.setValue(maps[0]); };
    VBox mapSelectorSection = new VBox(vGap, mapSelectorLabel, mapSelector);
    
    Label[] labels = {
      roadLengthLabel,
      mapSelectorLabel
    };
    for (Label l : labels) {
      l.setTextFill(textColor);
    }
    
    // Columns from the left
    this.addColumn(0, 
      spawnRateSection,
      spacer(),
      roadLengthSection
    );
    
    this.addColumn(1,
      minSpeedSection,
      spacer(),
      maxSpeedSection
    );
    
    this.addColumn(2,
      mapSelectorSection
    );
  }

  private InputParametersDTO getInputValues() {
    return new InputParametersDTO(
      this.spawnRateSection.getValue(),
      this.roadLengthControl.getValue(),
      this.minSpeedSection.getValue(),
      this.maxSpeedSection.getValue(),
      this.mapSelector.getValue()
    );
  }
}
