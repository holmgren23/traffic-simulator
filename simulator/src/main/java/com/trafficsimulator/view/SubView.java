package com.trafficsimulator.view;

/**
 * Subviews are views updated during the simulation.
 * The update method is called from the loop in the controller.
 * @see com.trafficsimulator.controller.Controller
 */
public interface SubView {

  public void update();
}
