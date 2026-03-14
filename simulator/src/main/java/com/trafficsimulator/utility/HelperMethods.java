package com.trafficsimulator.utility;

/**
 * A collection of static methods used by multiple packages.
 */
public class HelperMethods {
private static final int SPEED_MULTIPLIER = 30; // Assuming 1 unit of speed equals 30 km/h for display purposes

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
}
