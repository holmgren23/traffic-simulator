package com.trafficsimulator.startup;

import com.trafficsimulator.view.*;

import com.trafficsimulator.controller.Controller;

/** 
 * Class needed to start the program. Contains the startup sequence.
 * 
 * @see com.trafficsimulator.controller.Controller
 * @see com.trafficsimulator.view.MainView
 */
public class Main {
    /**
     * Startup sequence. Creates the controller and main view.
     * @param args not used
     */
    public static void main(String[] args) {
        // Confirming that the main method has started
        System.out.println("Starting simulator program...");

        MainView view = new MainView();
        Controller controller = new Controller(view);
        MainView.setController(controller);
        view.startWindow();

    }
}
