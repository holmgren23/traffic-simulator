package com.trafficsimulator.startup;

import com.trafficsimulator.view.*;
import com.trafficsimulator.model.*;
import com.trafficsimulator.controller.Controller;

public class Main {
    public static void main(String[] args) {
        // Confirming that the main method has started
        System.out.println("Main method started");

        System.out.println("Testing main method");
        TrafficSimulator ts = new TrafficSimulator();

        Road road1 = new Road(200);
        Road road2 = new Road(200);
        ts.getMap().addRoad(road1);
        ts.getMap().addRoad(road2);

        Obstacle trafficLight = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 100);
        Obstacle trafficLight2 = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 100);
        ts.getMap().addObstacle(trafficLight, road1);
        ts.getMap().addObstacle(trafficLight2, road2);

        Controller controller = new Controller(ts);
        MainView view = new MainView();
        MainView.setController(controller);
        MainView.setModel(ts);
        view.startWindow();

    }
}
