package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.geometry.HPos;

import com.trafficsimulator.utility.Constants;

public class SliderSection extends GridPane {
  private Slider inputSlider;
  private Label valueLabel;
  private String labelUnit;

  public SliderSection(String labelText, int min, int max, int initVal) {
    this(labelText, "", min, max, initVal);
  }

  public SliderSection(String labelText, String labelUnit, int min, int max, int initVal) {
    this.setStyle("-fx-padding: 2;");
    this.setHgap(0);
    this.setVgap(Constants.V_GAP);

    this.labelUnit = labelUnit;
    
    Label label = new Label(labelText + ": ");
    this.valueLabel = new Label(String.valueOf(initVal) + labelUnit);
    this.inputSlider = new Slider(min, max, initVal);
    
    Label[] labels = {label, valueLabel};
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
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
      valueLabel.setText(String.valueOf((int)rounded) + this.labelUnit);
    });
  }
  
  public Slider getSlider() {
    return this.inputSlider;
  }
  
  public double getValue() {
    return this.inputSlider.getValue();
  }
}
