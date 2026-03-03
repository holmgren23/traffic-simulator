package com.trafficsimulator.startup;

import com.trafficsimulator.view.*;
import com.trafficsimulator.model.Obstacle;
import com.trafficsimulator.model.ObstacleType;
import com.trafficsimulator.model.TrafficSimulator;
import com.trafficsimulator.controller.Controller;

public class Main {
    public static void main(String[] args) {
        // Confirming that the main method has started
        System.out.println("Main method started");

        System.out.println("Testing main method");
        TrafficSimulator ts = new TrafficSimulator();
        Obstacle trafficLight = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 100);
        ts.addObstacle(trafficLight);
        Controller controller = new Controller(ts);
        MainView view = new MainView();
        MainView.setController(controller);
        MainView.setModel(ts);
        view.startWindow();

    }
}
