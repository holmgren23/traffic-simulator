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
    private static Stage primaryStage;

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
        MainView.primaryStage = primaryStage;
        primaryStage.setTitle("Traffic Simulator");
        
        MenuView menuView = new MenuView(this, controller);

        StackPane root = new StackPane(menuView);
        root.setStyle("-fx-background-color: rgb(54, 66, 56);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

    public void showSimulation() {
        RoadView roadView = new RoadView(ts, windowWidth);
        StatisticsView statsView = new StatisticsView(ts);
        TransportView transportView = new TransportView(controller);
        SubView[] subViews = new SubView[] { roadView, statsView, transportView };

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
