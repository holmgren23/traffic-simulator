package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainView extends Application {
    // Dimentions for the window(pixels)
    private static int windowWidth = 0;
    private static int windowHeight = 0;
    private static Controller controller;
    private static TrafficSimulator ts;
    private static TrafficSimulator ts2;

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
    
    public static void setModel(TrafficSimulator ts, TrafficSimulator ts2) {
        MainView.ts = ts;
        MainView.ts2 = ts2;
    }

    // Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Traffic Simulator");

        System.out.println(ts);
        RoadView roadView = new RoadView(ts, ts2, windowWidth);
        StatisticsView statsView = new StatisticsView(ts);
        TransportView transportView = new TransportView(controller);
        SubView[] subViews = new SubView[] {roadView, statsView, transportView};

        controller.setSubViews(subViews);
        Thread simThread = new Thread(() -> controller.loop());
        simThread.setDaemon(true);
        simThread.start();

        StackPane root = new StackPane(roadView, statsView, transportView);
        StackPane.setAlignment(transportView, Pos.BOTTOM_CENTER);
        root.setStyle("-fx-background-color: rgb(15, 54, 22);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

}
