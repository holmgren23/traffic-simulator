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
        Road mainRoad = new Road(200);
        Obstacle trafficLight = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 100);
        ts.getMap().addRoad(mainRoad);
        ts.getMap().addObstacle(trafficLight, mainRoad);
        Controller controller = new Controller(ts);
        MainView view = new MainView();
        MainView.setController(controller);
        MainView.setModel(ts);
        view.startWindow();

    }
}
