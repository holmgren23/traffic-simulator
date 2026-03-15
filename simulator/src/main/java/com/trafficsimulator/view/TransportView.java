package com.trafficsimulator.view;

import com.trafficsimulator.controller.Controller;
import com.trafficsimulator.utility.Constants;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

/**
 * Contains user controls of a running simulation.
 * These allow the user to pause and resume, reset and stop
 * the simulation, as well as adjust the time scaling by
 * which the simulation runs.
 */
public class TransportView extends GridPane {
  private simButton pause, reset, stop;
  private Controller controller;
  private MainView mainView;
  private SliderSection timeScalingSection;
  private HBox buttonRow;
  
  /**
   * Shorthand to create the buttons used to control how the
   * simulation is shown.
   */
  private class simButton extends Button {
    /**
     * Creates a button and sets its properties according to
     * the parameters given.
     * @param text Text string shown on the button.
     * @param tooltip Text string shown as a tooltip when
     * hovering the mouse over the button.
     * @param eventHandler Event handler for when the button is pressed.
     */
    public simButton(String text, String tooltip, EventHandler<ActionEvent> eventHandler) {
      this.setText(text);
      this.setTooltip(new Tooltip(tooltip));
      this.setOnAction(eventHandler);
    }
  }

  /**
   * Creates UI elements and layout for the transport controls.
   * @param mainView Main window view to change view when stopping
   * the currently running simulation.
   * @param controller Controller object that handles the simulation clock.
   * This is needs to be told to stop the simulation.
   * @param timeScaling Time scaling value from the settings menu.
   * 
   * @see com.trafficsimulator.view.MainView
   * @see com.trafficsimulator.controller.Controller
   */
  public TransportView(MainView mainView, Controller controller, double timeScaling) {
    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle(Constants.BACKGROUND_CARD_STYLE);
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
        double offset = Math.log(Constants.TIME_SCALING_MIN_PERCENTAGE);
        double scale = Math.log(Constants.TIME_SCALING_MAX_PERCENTAGE);
        return Math.round(Math.pow(Math.E, (offset + labelVal * (scale - offset))));
      },
      false
    );
    timeScalingSection.setBehavior(sliderVal -> sliderVal);
    timeScalingSection.setChangeAction(() -> this.controller.setTimeScaling(timeScalingSection.getValue()));
    timeScalingSection.getSlider().setBlockIncrement(0.025f);
    timeScalingSection.setTooltip("Set the playback speed of the simulation [+/-]");

    this.add(buttonRow, 0, 1);
    this.add(timeScalingSection, 0, 0);
    GridPane.setHalignment(timeScalingSection, HPos.CENTER);
    GridPane.setMargin(timeScalingSection, new Insets(0,0,0,80));
  }
  
  /**
   * Defines and creates the buttons for pause/resume,
   * reset and stop, as well as the layout of these.
   */
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
    
    this.pause.setPrefWidth(100);
    this.pause.setStyle(Constants.BTN_STYLE_GREEN);
    this.reset.setPrefWidth(100);
    this.reset.setStyle(Constants.BTN_STYLE_ORANGE);
    this.stop.setPrefWidth(100);
    this.stop.setStyle(Constants.BTN_STYLE_RED);

    this.buttonRow = new HBox(pause, reset, stop);
    this.buttonRow.setSpacing(4);
  }
  
  /**
   * Defines and adds keyboard shortcuts for all
   * transport controls.
   */
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
