package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.model.StatSnapshot;
import com.trafficsimulator.utility.Constants;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Displays live simulation statistics as an overlay during a running simulation.
 * Updates each tick via the {@link SubView} interface.
 */
public class StatisticsView extends VBox implements SubView {
    private final TrafficSimulator simulator;
    private final Label timeElapsed = new Label();
    private final Label totalCarsLabel = new Label();
    private final Label avgSpeedLabel = new Label();
    private final Label stationaryCarsLabel = new Label();
    private final Label currentCarsLabel = new Label();
    private final Label vehicleInFlowLabel = new Label();
    private final Label vehicleOutFlowLabel = new Label();

    /**
     * Creates the statistics panel and sets up its layout and styling.
     * @param simulator The running simulation to read statistics from.
     */
    public StatisticsView(TrafficSimulator simulator) {
        this.simulator = simulator;

        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setStyle(Constants.BACKGROUND_CARD_STYLE);

        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(10);

        setupLabel(timeElapsed);
        setupLabel(totalCarsLabel);
        setupLabel(currentCarsLabel);
        setupLabel(avgSpeedLabel);
        setupLabel(stationaryCarsLabel);
        setupLabel(vehicleInFlowLabel);
        setupLabel(vehicleOutFlowLabel);

        // Left column
        grid.add(timeElapsed, 0, 0);
        grid.add(totalCarsLabel, 0, 1);
        grid.add(stationaryCarsLabel, 0, 2);
        grid.add(currentCarsLabel, 0, 3);

        // Right column
        grid.add(avgSpeedLabel, 1, 0);
        grid.add(vehicleInFlowLabel, 1, 1);
        grid.add(vehicleOutFlowLabel, 1, 2);

        this.getChildren().add(grid);
        update();
    }

    /**
     * Applies consistent font and color styling to a label.
     * @param label The label to style.
     */
    private void setupLabel(Label label) {
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 20));
    }

    /**
     * Reads the latest statistics snapshot from the simulation
     * and updates all displayed labels.
     */
    public void update() {
        StatSnapshot stats = simulator.getStats().getSnapshot();
        long totalSeconds = stats.totalElapsedTimeMS() / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String timeString = String.format("Time Elapsed: %02d:%02d:%02d", hours, minutes, seconds);

        timeElapsed.setText(timeString);
        totalCarsLabel.setText("Total cars spawned: " + stats.totalSpawned());
        currentCarsLabel.setText("Current cars: " + stats.currentCars());
        stationaryCarsLabel.setText("Stationary cars: " + stats.stationaryCars());
        avgSpeedLabel.setText(String.format("Avg Speed: %.2f km/h", stats.currentAvgSpeedKmh()));
        vehicleInFlowLabel.setText(String.format("Inflow: %.2f veh/min", stats.avgInFlow()));
        vehicleOutFlowLabel.setText(String.format("Outflow: %.2f veh/min", stats.avgOutFlow()));
    }
} 