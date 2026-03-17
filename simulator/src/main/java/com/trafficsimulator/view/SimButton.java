package com.trafficsimulator.view;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Shorthand to create the buttons used to control how the
 * simulation is shown.
 */
class SimButton extends Button {
  /**
   * Creates a button and sets its properties according to
   * the parameters given.
   * @param text Text string shown on the button.
   * @param tooltip Text string shown as a tooltip when
   * hovering the mouse over the button.
   * @param eventHandler Event handler for when the button is pressed.
   */
  public SimButton(String text, String tooltip, EventHandler<ActionEvent> eventHandler) {
    this.setText(text);
    this.setTooltip(new Tooltip(tooltip));
    this.setOnAction(eventHandler);
  }
}