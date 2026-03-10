package com.trafficsimulator.view;

import java.util.ArrayList;
import java.util.Arrays;

import com.trafficsimulator.controller.Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class TransportView extends HBox implements SubView {
  private final ArrayList<EventHandler<ActionEvent>> eventHandlers = new ArrayList<>(
    Arrays.asList(
    )
  );
  private simButton pause, reset, stop;
  private Controller controller;
  private MainView mainView;
  private Slider timeScalingSlider;
  
  private class simButton extends Button {
    public simButton(String text, String tooltip, EventHandler<ActionEvent> eventHandler) {
      this.setText(text);
      this.setTooltip(new Tooltip(tooltip));
      this.setOnAction(eventHandler);
    }
  }

  public TransportView(MainView mainView, Controller controller) {
    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setSpacing(4);
    
    this.mainView = mainView;
    this.controller = controller;

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
        MainView.showStatisticsPopup();
        this.mainView.showMenu();
      }
    );

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
      
        default:
          break;
      }
    });

    Label scalingLabel = new Label("Time Scaling:");
    scalingLabel.setTextFill(Color.WHITE);
    this.timeScalingSlider = new Slider(10, 500, 100);
    timeScalingSlider.setSnapToTicks(true);
    timeScalingSlider.setMajorTickUnit(50);
    timeScalingSlider.valueProperty().addListener((_, oldValue, newValue) -> {
      int TICK_LENGTH = 550 - (int)Math.round(newValue.doubleValue());
      this.controller.setTickLength(TICK_LENGTH);
      scalingLabel.setText("Tick length: " + TICK_LENGTH);
    });
    this.getChildren().addAll(pause, reset, stop, scalingLabel, timeScalingSlider);
  }

  public void update() {
  };
}
