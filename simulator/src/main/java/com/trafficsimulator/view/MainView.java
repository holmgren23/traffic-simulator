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
    private static Controller controller;
    private static TrafficSimulator ts;


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
    
    public static void setController(Controller controller) {
        MainView.controller = controller;
    }
    
    public static void setModel(TrafficSimulator ts) {
        MainView.ts = ts;
    }

    // Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traffic Simulator");

        System.out.println(ts);
        RoadView roadView = new RoadView(ts, windowWidth);

        controller.setRoadView(roadView);
        Thread simThread = new Thread(() -> controller.loop());
        simThread.setDaemon(true);
        simThread.start();

        StackPane root = new StackPane(roadView, statsView);
        root.setStyle("-fx-background-color: rgb(15, 54, 22);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

}
