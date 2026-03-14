package com.trafficsimulator.utility;

import javafx.scene.paint.Color;

/**
 * Constans values needed by multiple packages
 */
public class Constants {
  // Ticks and time
  public static final int TICK_LENGTH = 100;
  public static final int TICK_TIME_FACTOR = 10; // use this for more realistic speeds ??
  public static final int MAX_TICKS_PER_SPAWN = 10;
  
  // Time scaling value
  public static final int TIME_SCALING_MAX_PERCENTAGE = 1000;
  public static final int TIME_SCALING_MIN_PERCENTAGE = 30;
  public static final int TIME_SCALING_DEFAULT_PERCENTAGE = 100;

  // Styling
  public static final Color TEXT_COLOR = Color.SILVER;
  public static final int V_GAP = 12;
}
