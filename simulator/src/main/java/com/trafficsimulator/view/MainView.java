package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainView extends Application {
    //Dimensions for the window(pixels)
    private static int windowWidth = 0;
    private static int windowHeight = 0;
    private static Controller controller;
    private static TrafficSimulator ts;
    private static Stage primaryStage;

    //Constructor that allows setting custom window size
    public MainView(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    //Default constructor required by JavaFX, sets default window size if not
    //provided by constructor
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

    //Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        MainView.primaryStage = primaryStage;
        primaryStage.setTitle("Traffic Simulator");

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        windowWidth = (int)screenBounds.getWidth();
        windowHeight = (int)screenBounds.getHeight();
        
        this.showMenu();
    }

        
    public void showMenu() {
        MenuView menuView = new MenuView(this, controller);

        StackPane root = new StackPane(menuView);
        root.setStyle("-fx-background-color: rgb(54, 66, 56);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

    public void showSimulation() {
        RoadView roadView = new RoadView(ts, windowWidth, windowHeight - 100);

        StatisticsView statsView = new StatisticsView(ts);
        TransportView transportView = new TransportView(this, controller);
        SubView[] subViews = new SubView[] { roadView, statsView, transportView };

        controller.setSubViews(subViews);
        Thread simThread = new Thread(() -> controller.loop());
        simThread.setDaemon(true);
        simThread.start();

        StackPane root = new StackPane(roadView, statsView, transportView);
        StackPane.setAlignment(statsView, Pos.TOP_LEFT);
        StackPane.setMargin(statsView, new Insets(10, 0, 0, 10));

        StackPane.setAlignment(transportView, Pos.BOTTOM_CENTER);
        StackPane.setMargin(transportView, new Insets(0, 20, 20, 0));
        root.setStyle("-fx-background-color: rgb(15, 54, 22);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

    public void showSummaryScreen() {
    // Skapa den nya rapportvyn
    ReportView reportView = new ReportView(this, ts);
    
    // Skapa en ny scen och sätt den på huvudfönstret
    Scene scene = new Scene(reportView, windowWidth, windowHeight);
    primaryStage.setScene(scene);
    primaryStage.show();
}

}
