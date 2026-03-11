package com.trafficsimulator.view;

import com.trafficsimulator.controller.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;

public class TransportView extends GridPane {
  private simButton pause, reset, stop;
  private Controller controller;
  private MainView mainView;
  private SliderSection timeScalingSection;
  private HBox buttonRow;
  
  private class simButton extends Button {
    public simButton(String text, String tooltip, EventHandler<ActionEvent> eventHandler) {
      this.setText(text);
      this.setTooltip(new Tooltip(tooltip));
      this.setOnAction(eventHandler);
    }
  }

  public TransportView(MainView mainView, Controller controller, double timeScaling) {
    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setHgap(4);
    this.setVgap(8);
    
    this.mainView = mainView;
    this.controller = controller;

    this.createButtons();
    this.addKeyboardShortcuts();

    this.timeScalingSection = new SliderSection(
      "Time scaling",
      " %",
      0,
      1,
      timeScaling,
      labelVal -> {
        double offset = Math.log(30);
        double scale = Math.log(800);
        return Math.round(Math.pow(Math.E, (offset + labelVal * (scale - offset))));
      }
    );
    timeScalingSection.setBehavior(sliderVal -> sliderVal);
    timeScalingSection.setChangeAction(() -> this.controller.setTimeScaling(timeScalingSection.getValue()));
    timeScalingSection.getSlider().setBlockIncrement(0.025f);

    this.add(buttonRow, 0, 0);
    this.add(timeScalingSection, 0, 1);
    GridPane.setHalignment(timeScalingSection, HPos.CENTER);
  }
  
  private void createButtons() {
    this.pause = new simButton(
      "Pause",
      "Temporarily pause the simulation [K / SPACE BAR]",
      e -> {
        this.controller.toggleRunning();
        boolean isRunning = this.controller.isRunning();
        this.pause.setText(isRunning ? " Pause " : "Resume");
      }
    );
    this.reset = new simButton(
      "Reset",
      "Reset statistics and start simulation anew [BACKSPACE]",
      e -> this.controller.resetSimulation()
    );
    this.stop = new simButton(
      "Stop",
      "Stop simulation and show statistics summary [ESC]",
      e -> {
        this.controller.stopSimulation();
        this.mainView.showSummaryScreen();
      }
    );
    
    this.buttonRow = new HBox(pause, reset, stop);
    this.buttonRow.setSpacing(4);
  }
  
  private void addKeyboardShortcuts() {
    this.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
      if (e.getCode() != KeyCode.ENTER) { e.consume(); }
    });
    this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      switch (e.getCode()) {
        case KeyCode.SPACE:
        case KeyCode.K:
          this.pause.fire();
          e.consume();
          break;
        case KeyCode.BACK_SPACE:
          this.reset.fire();
          e.consume();
          break;
        case KeyCode.ESCAPE:
          this.stop.fire();
          e.consume();
          break;
        case KeyCode.PLUS:
          this.timeScalingSection.getSlider().increment();
          break;
        case KeyCode.MINUS:
          this.timeScalingSection.getSlider().decrement();
          break;
      
        default:
          break;
      }
    });
  }
}
