package com.trafficsimulator.utility;

public class HelperMethods {
private static final int SPEED_MULTIPLIER = 30; // Assuming 1 unit of speed equals 30 km/h for display purposes

  public static long ticksToTime(long ticks) {
    return (long) (ticks * Constants.TICK_LENGTH / 1000);
  }
  
  public static double speedToKMH(double speed) {
    return speed * SPEED_MULTIPLIER;
  }
}
