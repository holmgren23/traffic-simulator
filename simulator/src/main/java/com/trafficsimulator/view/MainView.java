package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    public static void showStatisticsPopup() {
        String reportText = ts.getFinalStats();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Simulation Statistics Report");
        alert.setHeaderText("Final Report:");
        alert.setContentText(reportText);
        
        //Tell the operating system that this is a popup window,
        //helpful on some linux systems
        ((Stage) alert.getDialogPane().getScene().getWindow()).initStyle(StageStyle.UTILITY);

        //Stop and wait for user to click OK
        alert.showAndWait(); 
    }

    //Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        MainView.primaryStage = primaryStage;
        primaryStage.setTitle("Traffic Simulator");
        
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

        StatisticsView statsView = new StatisticsView(ts, controller.getTickLength());
        TransportView transportView = new TransportView(this, controller);
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
