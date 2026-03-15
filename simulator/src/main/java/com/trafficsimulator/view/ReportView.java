package com.trafficsimulator.view;

import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;

import com.trafficsimulator.model.StatSnapshot;
import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.utility.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class ReportView extends VBox {

    public ReportView(MainView mainView, TrafficSimulator ts) {
        // Layout and styling
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.30);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 40;"
        );

        // Label
        Label title = new Label("SIMULATION SUMMARY");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
        title.setFont(new Font("Arial", 36));

        // Report-string from Statistics
        Label reportContent = new Label(ts.getFinalStats());
        Label settingsContent = new Label(ts.getSettingsReport());


        reportContent.setStyle("-fx-text-fill: #c0c0c0; -fx-font-family: 'Courier New'; -fx-font-size: 25px;");
        reportContent.setWrapText(true);
        reportContent.setMaxWidth(600);


        settingsContent.setStyle("-fx-text-fill: #c0c0c0; -fx-font-family: 'Courier New'; -fx-font-size: 25px;");
        settingsContent.setWrapText(true);
        settingsContent.setMaxWidth(600);

        HBox contentContainer = new HBox( 50, reportContent, settingsContent);
        contentContainer.setAlignment(Pos.CENTER);
        // Buttons
        Button menuBtn = new Button("Back to Menu");
        menuBtn.setPrefWidth(Constants.BTN_WIDTH);
        menuBtn.setStyle(Constants.BTN_STYLE_GREEN);
        menuBtn.setOnAction(e -> mainView.showMenu());

        Button exportBtn = new Button("Export to CSV");
        exportBtn.setPrefWidth(Constants.BTN_WIDTH);
        exportBtn.setStyle(Constants.BTN_STYLE_BLUE);
        exportBtn.setOnAction(e -> exportToCSV(ts));

        Button exitBtn = new Button("Exit Program");
        exitBtn.setPrefWidth(Constants.BTN_WIDTH);
        exitBtn.setStyle(Constants.BTN_STYLE_RED);
        exitBtn.setOnAction(e -> javafx.application.Platform.exit());

        HBox buttonContainer = new HBox(20, menuBtn,exportBtn, exitBtn);
        buttonContainer.setAlignment(Pos.CENTER);

        this.getChildren().addAll(title, contentContainer, buttonContainer);
    }

    private void exportToCSV(TrafficSimulator ts) {
        StatSnapshot stats = ts.getStats().getSnapshot();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Simulation Report");
        fileChooser.setInitialFileName("simulation_results.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        // Show save dialog
        File file = fileChooser.showSaveDialog(this.getScene().getWindow());
        
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Conversion of elapsed time fom ms to hh:mm:ss format
                long totalSeconds = stats.totalElapsedTimeMS() / 1000;
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                // Headers
                writer.println("Metric;Value;;Settings:");
                
                // Data rows
                writer.println("Total Elapsed Time;" + timeFormatted + ";;Min Speed;" + stats.minSpeed());
                writer.println("Total Ticks;" + stats.totalTicks() + ";;Max Speed;" + stats.maxSpeed());
                writer.println("Total Cars Spawned;" + stats.totalSpawned() + ";;Road Length;" + stats.roadLength());
                writer.println("Total Cars Exited;" + (stats.totalSpawned() - stats.currentCars()) + ";; Spawn Rate;" + stats.spawnRate());
                writer.println("Average Speed (km/h);" + String.format(Locale.forLanguageTag("sv"), "%.2f", stats.currentAvgSpeedKmh()));
                writer.println("Average Inflow (veh/m);" + String.format(Locale.forLanguageTag("sv"), "%.2f", stats.avgInFlow()));
                writer.println("Average Outflow (veh/m);" + String.format(Locale.forLanguageTag("sv"), "%.2f", stats.avgOutFlow()));
                
            } catch (Exception e) {
                System.err.println("Failed to save CSV: " + e.getMessage());
            }
        }
    }

}