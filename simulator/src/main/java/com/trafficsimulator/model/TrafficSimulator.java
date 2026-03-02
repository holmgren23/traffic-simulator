package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Road road;
    private int tickCount;
    private int spawnRate;
    private int nextID;
    private Random rand;
    private Statistics stats = new Statistics();

    //Deterministic random constructor for testing of random functions. Calls constructor below
    public TrafficSimulator() {
        this(new Random());
    }

    //Constructor for testing, Should make a new one for controller/view in future
    public TrafficSimulator(Random rand) {
        road = new Road(200);
        cars = new ArrayList<>();
        tickCount = 0;
        spawnRate = 5;
        nextID = 0;
        this.rand = rand;
    
    }

    public Road getRoad() {
        return road;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public int getCurrentTick() {
        return tickCount;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    // Finds gap from car to the car ahead
    public int gap(int position) {
        int i = position + 1;
        // Find gap to the next car
        while (i < road.getLength() && road.getCell(i) == null) {
            i++;
        }
        // If end of road, just go
        if (i >= road.getLength()) {
            return Integer.MAX_VALUE;
        }
        return i - position - 1;
    }

    // Update Positions for cars, Does not move
    private HashMap<Car, Integer> updateSpeeds() {
        HashMap<Car, Integer> newSpeeds = new HashMap<>();
        double slowProbability = 0.2;
        double speedupProbability = 0.3;
        for (Car car : cars) {
            int speed = car.getCurrentSpeed();
            int maxSpeed = car.getMaxSpeed();
            int position = car.getPosition();

            // New Speed calculation
            speed = Math.min(speed + 1, maxSpeed); // Speed up
            speed = Math.min(speed, gap(position)); // Break
            if (speed > 0 && rand.nextDouble() < slowProbability) {
                speed -= 1; // Random speed change
            }
            if (speed < maxSpeed && rand.nextDouble() < speedupProbability) {
                speed += 1; // Random speed change
            }
            newSpeeds.put(car, speed); // New final speed
        }

        return newSpeeds;
    }

    private void moveCars(HashMap<Car, Integer> newSpeeds) {
        ArrayList<Car> carsToRemove = new ArrayList<>();
        // Moves cars to new positions
        for (Car car : cars) {
            int oldPos = car.getPosition();
            int newSpeed = newSpeeds.get(car);
            int newPos = oldPos + newSpeed;

            // Remove from old cell
            road.setCell(oldPos, null);
            // If car exits road - Currently removes, can be changed to loop
            if (newPos >= road.getLength()) {
                carsToRemove.add(car); // Collects all cars that should be removed
                continue;
            }
            // Place in new cell
            car.setPosition(newPos);
            car.setCurrentSpeed(newSpeed);
            road.setCell(newPos, car);
        }
        cars.removeAll(carsToRemove); // Removes cars outside road
    }

    // Function spawns cars based on spawnrate per tick.
    private void spawnCar() {
        if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
            Car newCar = new Car(nextID, 0, rand.nextInt(1, 10));
            cars.add(newCar);
            road.setCell(0, newCar);
            nextID++;
            stats.incrementTotalCarsSpawned();
        }
    }

    // Tick update. Should be callable by controller
    public void tickUpdate() {
        tickCount++;
        moveCars(updateSpeeds());
        spawnCar();
        stats.update(cars);
    }

    public Statistics getStats() {
        return stats;
    }

}
