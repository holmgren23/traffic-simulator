package com.trafficsimulator.utility;

/**
 * A collection of static methods used by multiple packages.
 */
public class HelperMethods {
private static final int SPEED_MULTIPLIER = 10;
private static final int SECS_IN_MIN = 60;
private static final int MS_IN_S = 1000;

  /**
   * Converts a number of ticks to the number of seconds
   * they represent.
   * @param ticks Time in ticks
   * @return Time in seconds
   */
  public static int ticksToSec(long ticks) {
    return (int) (ticks * Constants.S_IN_TICK);
  }
  
  /**
   * Converts a number of ticks to the time in milliseconds
   * they represent.
   * @param ticks Time in ticks
   * @return Time in milliseconds
   */
  public static long ticksToMs(long ticks) {
    return (long) (ticksToSec(ticks) * MS_IN_S);
  }
  
  /**
   * Converts a number of seconds to the number of
   * ticks represented.
   * @param sec Time in seconds
   * @return Time in ticks
   */
  public static int secToTicks(long sec) {
    return (int) (sec / Constants.S_IN_TICK);
  }
  
  /**
   * Converts a number of ticks to the number of minutes
   * they represent.
   * @param ticks Time in ticks
   * @return Time in minutes
   */
  public static int ticksToMin(long ticks) {
    return (int) ticksToSec(ticks) / SECS_IN_MIN;
  }
  
  /**
   * Converts a number of minutes to the number
   * of ticks represented.
   * @param min Time in minutes
   * @return Time in ticks
   */
  public static int minToTicks(int min) {
    return (int) secToTicks(min * SECS_IN_MIN);
  }
  
  /**
   * Converts a length in meters to the corresponding
   * number of road cells.
   * @param meters Length in meters
   * @return Length in road cells
   */
  public static int oneHundredMToCells(double meters) {
    return (int) (meters * 100 / Constants.METERS_IN_CELL);
  }
  
  /**
   * Converts a number of road cells to a length in
   * meters.
   * @param cells Length in road cells
   * @return Length in meters
   */
  public static int cellsToM(int cells) {
    return (int) (cells * Constants.METERS_IN_CELL);
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
  
  /**
   * Converts a road length value to a string representation, or "N/A"
   * if the map does not support adjustable road length.
   * @param map Name of the map to check whether it supports adjustable road length
   * @param roadLength Length of the road in meters
   * @return Appropriate string representation to be shown in the statistics summary
   */
  public static String roadLengthToString(String map, int roadLength) {
    return Constants.MAP_HAS_ADJUSTABLE_ROAD_LENGTH.get(map.toString()) ? Integer.toString(roadLength) + " m" : "N/A";
  }
}
