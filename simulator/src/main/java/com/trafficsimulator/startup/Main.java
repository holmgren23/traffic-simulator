package com.trafficsimulator.startup;
import com.trafficsimulator.model.TrafficSimulator;

public class Main {
    public static void main(String[] args) {
       
        System.out.println("Testing main method");
        TrafficSimulator ts = new TrafficSimulator();
        for(int i = 0; i < 200; i++){
            ts.tickUpdate();
            ts.printRoad();
        }
    }

}
