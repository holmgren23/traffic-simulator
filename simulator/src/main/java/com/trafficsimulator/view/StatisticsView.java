package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.model.StatSnapshot;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StatisticsView extends VBox implements SubView {
    private final TrafficSimulator simulator;
    private final Label timeElapsed = new Label();
    private final Label totalCarsLabel = new Label();
    private final Label avgSpeedLabel = new Label();
    private final Label stationaryCarsLabel = new Label();
    private final Label currentCarsLabel = new Label();
    private final Label vehicleInFlowLabel = new Label();
    private final Label vehicleOutFlowLabel = new Label();


    public StatisticsView(TrafficSimulator simulator) {
        this.simulator = simulator;
        // Size for statistics panel
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
 
        this.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.7);" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: #666;" +
            "-fx-border-width: 1;"
        );
        
        // GridPane for layout 
        GridPane grid = new GridPane();
        grid.setHgap(40);
        grid.setVgap(10);

        // Setup labels
        setupLabel(timeElapsed);
        setupLabel(totalCarsLabel);
        setupLabel(currentCarsLabel);
        setupLabel(avgSpeedLabel);
        setupLabel(stationaryCarsLabel);
        setupLabel(vehicleInFlowLabel);
        setupLabel(vehicleOutFlowLabel);
        // 1st column
        grid.add(timeElapsed, 0, 0);
        grid.add(totalCarsLabel, 0, 1);
        grid.add(stationaryCarsLabel, 0, 2);
        grid.add(currentCarsLabel, 0, 3);
        // 2nd column
        grid.add(avgSpeedLabel, 1, 0);
        grid.add(vehicleInFlowLabel, 1, 1);
        grid.add(vehicleOutFlowLabel, 1, 2);

        this.getChildren().add(grid);
        update(); 
    }
    // Method to setup label styling
    private void setupLabel(Label label) {
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 20));
    }

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
        vehicleInFlowLabel.setText(String.format("Inflow: %.2f veh/minute", stats.avgInFlow()));
        vehicleOutFlowLabel.setText(String.format("Outflow: %.2f veh/minute", stats.avgOutFlow()));
    
    }
} 