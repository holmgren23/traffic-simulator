package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.geometry.HPos;

import com.trafficsimulator.utility.Constants;

public class SliderSection extends GridPane {
  private Slider inputSlider;
  private Label descriptionLabel, valueLabel;
  private String labelUnit;
  private double value;
  private UnaryNumericOperation scaler;
  private boolean onlyScaleLabel;
  private PassableAction changeAction;

  public SliderSection(String labelText, double min, double max, double initVal) {
    this(labelText, "", min, max, initVal);
  }
  
  public SliderSection(
    String labelText,
    String labelUnit,
    double min,
    double max,
    double initVal,
    UnaryNumericOperation scaler,
    boolean onlyScaleLabel
  ) {
    this.scaler = scaler;
    this.onlyScaleLabel = onlyScaleLabel;
    this(labelText, labelUnit, min, max, initVal);
  }

  public SliderSection(String labelText, String labelUnit, double min, double max, double initVal) {
    this.setStyle("-fx-padding: 2;");
    this.setHgap(0);
    this.setVgap(Constants.V_GAP);

    this.labelUnit = labelUnit;
    double scaledValue = this.scaleValue(initVal);
    this.value = this.onlyScaleLabel ? initVal : scaledValue;
    
    this.descriptionLabel = new Label(labelText + ": ");
    this.valueLabel = new Label(this.valueToLabel(scaledValue));
    this.inputSlider = new Slider(min, max, initVal);
    
    Label[] labels = {this.descriptionLabel, valueLabel};
    for (Label l : labels) {
      l.setTextFill(Constants.TEXT_COLOR);
      this.add(l, 0, 0);
    }
    this.add(inputSlider, 0, 1);

    GridPane.setHalignment(valueLabel, HPos.RIGHT);
    //GridPane.setHgrow(inputSlider, Priority.ALWAYS);
  }
  
  public void setBehavior(UnaryNumericOperation expr) {
    this.inputSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      double rounded = expr.operate(newVal.doubleValue());
      inputSlider.setValue(rounded);
      double scaled = this.scaleValue(rounded);
      this.value = (this.onlyScaleLabel ? rounded : scaled);
      valueLabel.setText(this.valueToLabel(scaled));
      if (this.changeAction != null) {
        this.changeAction.run();
      }
    });
  }
  
  public void setChangeAction(PassableAction a) {
    this.changeAction = a;
  }
  
  private double scaleValue(double v) {
    if (this.scaler == null) {
      return v;
    }
    else {
      return this.scaler.operate(v);
    }
  }
  
  private String valueToLabel(double v) {
    return String.valueOf((int)v) + this.labelUnit; 
  }

  public void setScaling(UnaryNumericOperation expr) {
    this.scaler = expr;
  }
  
  public Slider getSlider() {
    return this.inputSlider;
  }
  
  public double getValue() {
    return this.value;
  }
  
  public void setTooltip(Tooltip tooltip) {
    this.inputSlider.setTooltip(tooltip);
    this.descriptionLabel.setTooltip(tooltip);
    this.valueLabel.setTooltip(tooltip);
  }

  public void setTooltip(String text) {
    this.setTooltip(new Tooltip(text));
  }
}
