package com.trafficsimulator.view;

import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.geometry.HPos;
import javafx.geometry.Pos;

import com.trafficsimulator.utility.Constants;

/**
 * Layout and common functionality of a slider
 * with labels for name and current value.
 */
public class SliderSection extends GridPane {
  private Slider inputSlider;
  private Label descriptionLabel, valueLabel;
  private String labelUnit;
  private double value;
  private UnaryNumericOperation scaler;
  private boolean onlyScaleLabel;
  private PassableAction changeAction;

  /**
   * Shortcut for creating a standard slider section without a unit for its value.
   * @param labelText Setting name/description
   * @param min Minimum slider value
   * @param max Maximum slider value
   * @param initVal Initial slider value
   */
  public SliderSection(String labelText, double min, double max, double initVal) {
    this(labelText, "", min, max, initVal);
  }
  
  /**
   * 
   * @param labelText Setting name/description
   * @param labelUnit Suffix for the value label, commonly used to display a unit
   * @param min Minimum slider value
   * @param max Maximum slider value
   * @param initVal Initial slider value
   * @param scaler Lambda expression for scaling the output value (for
   * example to use a logarithmic scale)
   * @param onlyScaleLabel Set true to scale the label value, but not the 
   * output value used by the model.
   */
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

  /**
   * Creates a standard slider section.
   * @param labelText Setting name/description
   * @param labelUnit Suffix for the value label, commonly used to display a unit
   * @param min Minimum slider value
   * @param max Maximum slider value
   * @param initVal Initial slider value
   */
  public SliderSection(String labelText, String labelUnit, double min, double max, double initVal) {
    this.setStyle("-fx-padding: 2;");
    this.setHgap(0);
    this.setVgap(Constants.V_GAP);
    this.setAlignment(Pos.CENTER);

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
  }
  
  /**
   * Defines the quantize behavior of the slider. Commonly used to
   * make the slider jump to integer values.
   * @param expr A lambda expression to operate on the slider value
   * before the slider, value and label are changed.
   */
  void setBehavior(UnaryNumericOperation expr) {
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
  
  /**
   * Sets a custom action that runs after the input behavior
   * defined in the {@link #setBehaviour() setBehavior} method.
   * Used by the transport controls for setting the time
   * scaling while a simulation is running.
   * @param a Action to run when slider is changed.
   */
  void setChangeAction(PassableAction a) {
    this.changeAction = a;
  }
  
  /**
   * Scales the slider value only if a scaler is set.
   * @param v Value to be scaled.
   * @return Scaled value if a scaler is set, otherwise 
   * same value as input.
   */
  private double scaleValue(double v) {
    if (this.scaler == null) {
      return v;
    }
    else {
      return this.scaler.operate(v);
    }
  }
  
  /**
   * Converts the slider section value to a string
   * to use in the value label.
   * @param v Value to show as string.
   * @return String representation of v, with a
   * value suffix if set.
   */
  private String valueToLabel(double v) {
    return String.valueOf((int)v) + this.labelUnit; 
  }

  /**
   * Set a scaler for the input value. Useful to get
   * for example logarithmic behavior of the slider.
   * @param expr Lambda expression for scaling the input value.
   */
  void setScaling(UnaryNumericOperation expr) {
    this.scaler = expr;
  }
  
  /**
   * For direct access to the slider.
   * @return Slider in the SliderSection
   */
  Slider getSlider() {
    return this.inputSlider;
  }
  
  /**
   * The user input value.
   * @return The slider value after quantize and scaler.
   */
  double getValue() {
    return this.value;
  }
  
  /** 
   * Allows a value to be set, used for restoring previous
   * settings after a simulation has finished. Automatically
   * scales the label value if applicable.
   * @param value Settings value
   */
  void setValue(double value) {
    this.inputSlider.setValue(value);
    double scaledValue = this.scaleValue(value);
    this.value = this.onlyScaleLabel ? value : scaledValue;
    this.valueLabel.setText(this.valueToLabel(scaledValue));
  }
  
  /**
   * Set a tooltip for all UI elements in the SliderSection.
   * Useful to give a more detailed description of the input
   * value.
   * @param tooltip For slider and labels.
   */
  void setTooltip(Tooltip tooltip) {
    this.inputSlider.setTooltip(tooltip);
    this.descriptionLabel.setTooltip(tooltip);
    this.valueLabel.setTooltip(tooltip);
  }

  /**
   * Override of {@Link #setTooltip() setTooltip} method
   * that takes a String as input.
   * @param text String to show as tooltip for slider and
   * labels.
   */
  void setTooltip(String text) {
    this.setTooltip(new Tooltip(text));
  }
}
