package com.trafficsimulator.utility;

import java.util.Map;

import javafx.scene.paint.Color;

/**
 * Constant values needed by multiple packages
 * These are used to get consistency in the model's statistics
 * and the user input in the view package.
 * The categories included here are:
 * 
 * Ticks and time
 * Lengths
 * Spawn probability
 * Time scaling value
 * Maps
 */
public class Constants {
  // Ticks and time
  public static final int TICK_LENGTH = 100;
  public static final double S_IN_TICK = 2.7;
  public static final int PROGRESS_UPDATE_TICKS = 1000;
  
  // Lengths
  public static final double METERS_IN_CELL = 7.5;
  
  // Spawn probability
  public static final int SPAWN_PROBABILITY_MIN = 2;
  public static final int SPAWN_PROBABILITY_MAX = 80;
  public static final int SPAWN_PROBABILITY_DEFATULT = 25;
  
  // Time scaling value
  public static final int TIME_SCALING_MAX_PERCENTAGE = 1000;
  public static final int TIME_SCALING_MIN_PERCENTAGE = 30;
  public static final int TIME_SCALING_DEFAULT_PERCENTAGE = 100;

  //Button labels
  public static final String BTN_TEXT_EXIT = "Exit";
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
    "Grid of roads", false
  );
}
