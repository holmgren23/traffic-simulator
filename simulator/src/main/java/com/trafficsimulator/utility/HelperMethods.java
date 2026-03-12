package com.trafficsimulator.utility;

public class HelperMethods {

  public static long ticksToTime(long ticks) {
    return (long) (ticks * Constants.TICK_LENGTH / 1000);
  }
}
