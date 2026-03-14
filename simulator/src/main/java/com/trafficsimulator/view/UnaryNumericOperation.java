package com.trafficsimulator.view;

/**
 * Interface used to pass a simple lambda expression
 * to the SliderSection class. These are mainly used
 * to quantize the slider input to discrete values.
 * @see com.trafficsimulator.view.SliderSection
 */
@FunctionalInterface
public interface UnaryNumericOperation {
  double operate(double v);
}