package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.util.Random;

public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Road road;
    private int tickCount;
    private int spawnRate;
    private int nextID;
    private Random rand;

    public TrafficSimulator() {
        road = new Road(50);
        cars = new ArrayList<>();
        tickCount = 0;
        spawnRate = 5;
        nextID = 0;
        rand = new Random();
    }

    //Finds gap from car to the car ahead, only checks up to the cars max speed
    public int gap(Car currentCar) {
            int gap = 0;
            int position = currentCar.getPosition();
            int maxSpeed = currentCar.getMaxSpeed();

            int upperBound = Math.min(road.getLength(), position + 1 + maxSpeed);

            for (int i = position + 1; i < upperBound; i++) {
                if (road.getCell(i) == null) {
                    gap++;
                } else {
                    break;
                }
            }
        return gap;
    }

    //Update Positions for cars, Does not move
    private HashMap<Car, Integer> updateSpeeds() {
        HashMap<Car, Integer> newSpeeds = new HashMap<>();
        double slowProbability = 0.4;
        for (Car car : cars) {
            int speed = car.getCurrentSpeed();
            int maxSpeed = car.getMaxSpeed();



            //New Speed calculation
            speed = Math.min(speed + 1, maxSpeed); //Speed up
            speed = Math.min(speed, gap(car)); //Break
            if (speed > 0 && rand.nextDouble() < slowProbability) {
                speed -= 1; //Random speed change
            }
            newSpeeds.put(car, speed); //New final speed
        }

        return newSpeeds;
    }

    private void moveCars(HashMap<Car, Integer> newSpeeds) {
    //Moves cars to new positions
    for (Car car:cars) {
        int oldPos = car.getPosition();
        int newSpeed = newSpeeds.get(car);
        int newPos = oldPos + newSpeed;

        //Remove from old cell
        road.setCell(oldPos, null);

        //If car exits road - Currently removes, can be changed to loop
        if (newPos >= road.getLength()) {
            cars.remove(car); //Should possibly collect all cars that are to be removed before removal
            continue;
        }

        //Place in new cell
        car.setPosition(newPos);
        car.setCurrentSpeed(newSpeed);
        road.setCell(newPos, car);
        }
    }

    private void spawnCar() {
        if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
            Car newCar = new Car(nextID, 0);
            cars.add(newCar);
            road.setCell(0, newCar);
            nextID++;
        }
    }

    public void tickUpdate() {
        tickCount++;
        spawnCar();
        moveCars(updateSpeeds());
    }
}
