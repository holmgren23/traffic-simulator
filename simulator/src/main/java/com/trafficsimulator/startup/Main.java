package com.trafficsimulator.startup;
import com.trafficsimulator.view.MainView;

public class Main {
    public static void main(String[] args) {
        // Confirming that the main method has started
        System.out.println("Main method started");
        
        // Here we need to create Controller object
        // Controller controller = new Controller();

        // Here we need to create Model object
        // Model model = new Model();


        MainView view = null;

        try {
            view = new MainView();
        } catch (Exception e) {
            System.err.println("MainView object could not be initialized");
        }
        
        if (view != null) {
            view.startWindow();
        }

        
    }

}
