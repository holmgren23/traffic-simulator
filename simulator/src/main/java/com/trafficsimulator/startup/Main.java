package com.trafficsimulator.startup;

import com.trafficsimulator.model.*;


public class Main {
    public static void main(String[] args) {
        Road road = new Road(50);
        Car car1 = new Car(1, 0);
        road.setCell(0, car1);

        System.out.println(road.getCell(0));
        System.out.println(road.getCell(1)); 
        System.out.println("Testing main method");
        
    }

}
