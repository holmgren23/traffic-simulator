package com.trafficsimulator.utility;

import java.util.Map;

import javafx.scene.paint.Color;

/**
 * Constant values needed by multiple packages
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

  // Card/panel background style
  public static final String BACKGROUND_CARD_STYLE =
      "-fx-background-color: rgba(0, 0, 0, 0.40);" +
      "-fx-background-radius: 14;" +
      "-fx-padding: 12;";

  // Button styles
  public static final String BTN_STYLE_GREEN  = "-fx-base: #164e0e;"; // start/pause/resume/backtomenu
  public static final String BTN_STYLE_BLUE   = "-fx-base: #252a72;"; // export
  public static final String BTN_STYLE_RED    = "-fx-base: #b31111;"; // exit/stop
  public static final String BTN_STYLE_ORANGE = "-fx-base: #b38600;"; // reset 
  public static final double BTN_WIDTH = 150;
  
  // Maps
  public static final Map<String, Boolean> MAP_HAS_ADJUSTABLE_ROAD_LENGTH = Map.of(
    "Highway", true,
    "One way street", true,
    "T Intersection", false,
    "Autobahn", true,
    "Intersection Loop", false,
    "Grid of roads", false
  );
}
