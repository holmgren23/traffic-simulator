package com.trafficsimulator.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainViewTest extends Application {
    // Dimentions for the window(pixels)
    private static int windowWidth = 0;
    private static int windowHeight = 0;

    
    // Constructor that allows setting custom window size
    public MainViewTest(int width, int height) {
        windowWidth = width;
        windowHeight = height;
    }
    
    // Default constructor required by JavaFX, sets default window size if not provided by constructor
    public MainViewTest(){
        if (windowWidth == 0) windowWidth = 1280;
        if (windowHeight == 0) windowHeight = 720;
    }

    // Launches the JavaFX application
    public void startWindow() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        // Windowtitle
        primaryStage.setTitle("MainView");

        // Root container that will hold all UI elements <--- THIS WE NEED TO CHANGE LATER
        StackPane root = new StackPane();

        // Scene that hold sthe root container and sets the window size
        Scene window = new Scene(root, windowWidth, windowHeight);

        // Setting the scene to the stage and displaying the window
        primaryStage.setScene(window);
        primaryStage.show();
    }

}
