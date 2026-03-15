package com.trafficsimulator.view;

import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The main window of the program.
 */
public class MainView extends Application {
    //Dimensions for the window(pixels)
    private static int windowWidth = 0;
    private static int windowHeight = 0;
    private static Controller controller;
    private static TrafficSimulator ts;
    private static Stage primaryStage;

    /**
     * Creates a main view with the given width and height
     * @param width Window width
     * @param height Window height
     */
    public MainView(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }

    /**
     * Default constructor required by JavaFX, sets default window size if not
     * provided by constructor
     */
    public MainView() {
        if (windowWidth == 0)
            windowWidth = 1280;
        if (windowHeight == 0)
            windowHeight = 720;
    }

    /**
     * Sets the controller to receive user input
     * @param controller Used by any views that takes user input
     */
    public static void setController(Controller controller) {
        MainView.controller = controller;
    }

    /**
     * Sets the simulation model. The model is created by the controller, 
     * which allows different simulations to run without restarting the program.
     * @param ts
     */
    public static void setModel(TrafficSimulator ts) {
        MainView.ts = ts;

    }

    /**
     * Launches the JavaFX application
     */
    public void startWindow() {
        launch();
    }

    /**
     * JavaFX uses this method to run the GUI thread.
     * It sets the window size to fit the screen, then
     * starts the settings menu.
     */
    @Override
    public void start(Stage primaryStage) {
        MainView.primaryStage = primaryStage;
        primaryStage.setTitle("Traffic Simulator");

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        windowWidth = (int)screenBounds.getWidth();
        windowHeight = (int)screenBounds.getHeight();
        
        this.showMenu();
    }

        
    /**
     * Creates and shows the user settings menu.
     */
    public void showMenu() {
        MenuView menuView = new MenuView(controller);

        Label title = new Label("Trafficsimulator");
        title.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold;");
        title.setFont(new Font("Arial", 42));

        VBox card = new VBox(24, title, menuView);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.30);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 40;"
        );
        card.setMaxWidth(Region.USE_PREF_SIZE);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: rgb(54, 66, 56);");
        Scene window = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(window);
        primaryStage.show();
    }

    /**
     * Creates and shows the simulation visuals. Started from
     * the user settings menu.
     * @param timeScaling
     */
    public void showSimulation(double timeScaling) {
        RoadView roadView = new RoadView(ts, windowWidth, windowHeight - 100);

        StatisticsView statsView = new StatisticsView(ts);
        TransportView transportView = new TransportView(this, controller, timeScaling);
        SubView[] subViews = new SubView[] { roadView, statsView};

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

    /**
     * Creates and shows the final statistics summary.
     * Allows navigation back to settings menu.
     */
    public void showSummaryScreen() {
        ReportView reportView = new ReportView(this, ts);
        reportView.setMaxWidth(Region.USE_PREF_SIZE);
        reportView.setMaxHeight(Region.USE_PREF_SIZE);

        StackPane root = new StackPane(reportView);
        root.setStyle("-fx-background-color: rgb(54, 66, 56);");
        Scene scene = new Scene(root, windowWidth, windowHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
