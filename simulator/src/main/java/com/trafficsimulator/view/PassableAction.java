package com.trafficsimulator.view;

/**
 * Interface used to pass a simple lambda expression
 * to the SliderSection class, used to scale the slider
 * input.
 * @see com.trafficsimulator.view.SliderSection
 */
@FunctionalInterface
public interface PassableAction {
  void run();
}