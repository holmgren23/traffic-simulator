package com.trafficsimulator.model;

import java.util.ArrayList;

public class TrafficSimulator {
    private ArrayList<Car> cars = new ArrayList<>();
    private Road road;

    private int tickCount = 0;
    private int spawnRate = 5;
    private int nextID = 0;

    public TrafficSimulator() {
        road = new Road(50);
    }

    public void tickUpdate() {
        tickCount++;
        if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
            Car newCar = new Car(nextID, 0); // cars.size() kommer inte funka i längden, behöver en bättre lösning för att ge varje bil ett unikt ID
            cars.add(newCar);
            road.setCell(0, newCar);
            nextID++; //Löser problemet
        }
        for(Car car:cars){
           int position = car.getPosition();
           int speed = car.getCurrentSpeed();
           if (speed == 0) {
            car.setCurrentSpeed(car.getMaxSpeed());
           }
           for (int i = position + 1; i < position + speed; i++){ // tror det är problem med att flytta bilen, kanske ska ha en loop för gap och en för move
                  
            if (road.getCell(i) == null){
                      car.setPosition(i);
                      road.setCell(position, null); //tar bort bilen från den gamla positionen
                      road.setCell(i, car); //måste ta bort bilen från den gamla positionen också
                  } 
                    else {
                        car.setPosition(i-1);
                        car.setCurrentSpeed(0);
                        break;
                    }
           } 
        }
    }



}
