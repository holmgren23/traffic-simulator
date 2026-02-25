package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainView extends Application {
    // Dimentions for the window(pixels)
    private static int windowWidth = 0;
    private static int windowHeight = 0;

    // Constructor that allows setting custom window size
    public MainView(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    // Default constructor required by JavaFX, sets default window size if not
    // provided by constructor
    public MainView() {
        if (windowWidth == 0)
            windowWidth = 1280;
        if (windowHeight == 0)
            windowHeight = 720;
    }

    // Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    private static TrafficSimulator simulator;

    public static void setSimulator(TrafficSimulator ts) {
        simulator = ts;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traffic Simulator");

        RoadView roadView = new RoadView(simulator, windowWidth);

        Controller controller = new Controller(simulator, roadView);
        Thread simThread = new Thread(() -> controller.loop());
        simThread.setDaemon(true);
        simThread.start();

        StackPane root = new StackPane(roadView);
        root.setStyle("-fx-background-color: green;");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

}
