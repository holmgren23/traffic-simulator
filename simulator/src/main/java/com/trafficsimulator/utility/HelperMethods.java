package com.trafficsimulator.utility;

/**
 * A collection of static methods used by multiple packages.
 */
public class HelperMethods {
private static final int SPEED_MULTIPLIER = 10; // Assuming 1 unit of speed equals 30 km/h for display purposes

  /**
   * Converts a number of ticks to a real world time value.
   */
  public static long ticksToTime(long ticks) {
    return (long) (ticks * Constants.TICK_LENGTH / 1000);
  }
  
  /**
   * Converts a speed in the simulation model to a real world value
   * in km/h
   * @param speed speed in cells per tick
   * @return speed in km/h
   */
  public static double speedToKMH(double speed) {
    return speed * SPEED_MULTIPLIER;
  }
  
  /**
   * Simple method for converting a probability between 0.0 and 1.0
   * to a percentage (truncated to an integer value).
   * @param probability Value between 0.0 and 1.0
   * @return Percentage between 0 and 100
   */
  public static int probabilityToPercent(double probability) {
    return (int)(probability * 100);
  }

  /**
   * Simple method for converting a percentage between 0 and 100
   * to a probability between 0.0 and 1.0.
   * @param percent Integer value between 0 and 100
   * @return Probability between 0.0 and 1.0
   */
  public static double percentToProbability(int percent) {
    return percent / 100.0;
  }
}
