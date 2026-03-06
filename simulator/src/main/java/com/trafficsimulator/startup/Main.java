package com.trafficsimulator.startup;

import com.trafficsimulator.view.*;
import com.trafficsimulator.model.*;

import java.util.Random;

import com.trafficsimulator.controller.Controller;

public class Main {
    public static void main(String[] args) {
        // Confirming that the main method has started
        System.out.println("Main method started");

        System.out.println("Testing main method");
<<<<<<< HEAD
        TrafficSimulator ts = new TrafficSimulator(new Random(), Map.createHighway(200));

        Controller controller = new Controller(ts);
=======

        Controller controller = new Controller();
>>>>>>> 26c81dc5396405ff5e25ab802ece2ecef9e23a23
        MainView view = new MainView();
        MainView.setController(controller);
        view.startWindow();

    }
}
