package com.trafficsimulator.utility;

public class HelperMethods {
  public static final int TICK_LENGTH = 100;
  public static final int TICK_TIME_FACTOR = 10; // use this for more realistic speeds ??

  public static long ticksToTime(long ticks) {
    return (long) (ticks * TICK_LENGTH / 1000);
  }
}
