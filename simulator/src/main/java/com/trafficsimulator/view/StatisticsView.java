package com.trafficsimulator.view;

import com.trafficsimulator.model.Statistics;
import com.trafficsimulator.model.TrafficSimulator;
import javafx.scene.control.Label;
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
    private final int tickLength;

    public StatisticsView(TrafficSimulator simulator, int TICK_LENGTH) {
        this.simulator = simulator;
        this.tickLength = TICK_LENGTH;
    

        this.setSpacing(5);
        this.setStyle("-fx-padding: 15;");
        this.setMaxWidth(300);
   
        setupLabel(timeElapsed);
        setupLabel(totalCarsLabel);
        setupLabel(currentCarsLabel);
        setupLabel(avgSpeedLabel);
        setupLabel(stationaryCarsLabel);

        this.getChildren().addAll(timeElapsed, totalCarsLabel, avgSpeedLabel, stationaryCarsLabel, currentCarsLabel);
        update(); 
    }

    private void setupLabel(Label label) {
        label.setTextFill(Color.WHITE);
        label.setFont(new Font("Arial", 20));
    }

    public void update() {
        Statistics stats = simulator.getStats();
        long totalSeconds = (stats.getTotalTicks() * (long)tickLength) / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        String timeString = String.format("Time Elapsed: %02d:%02d:%02d", hours, minutes, seconds);

        timeElapsed.setText(timeString);
        totalCarsLabel.setText("Total cars spawned: " + stats.getTotalCarsSpawned());
        currentCarsLabel.setText("Current cars#: " + stats.getCurrentAmountOfCars());
        avgSpeedLabel.setText(String.format("Average Speed: %.2f kmh", stats.getAverageSpeed()));
        stationaryCarsLabel.setText("Stationary cars: " + stats.getStationaryCars());
    }
}