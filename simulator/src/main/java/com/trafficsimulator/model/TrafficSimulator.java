package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Map map;
    private int tickCount;
    private int spawnRate;
    private int nextID;
    private Random rand;
    private Statistics stats;
    private double slowProbability = 0.2;
    private double speedupProbability = 0.3;

    // Deterministic random constructor for testing of random functions. Calls constructor below
    public TrafficSimulator() {
        this(new Random());
    }

    // Constructor for testing, Should make a new one for controller/view in future
    public TrafficSimulator(Random rand) {
        spawnRate = 5;
        this.rand = rand;
        this.initializeSimulation();
    }
    
    private void initializeSimulation() {
        road = new Road(200);
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
        this.rand = rand;
        stats = new Statistics();
    }

    //Getters
    public ArrayList<Car> getCars() {
        return cars;
    }

    public int getCurrentTick() {
        return tickCount;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public Map getMap() {
        return map;
    }

    // Calculates Positions for cars, Does not move
    private HashMap<Car, Integer> updateSpeeds() {
        HashMap<Car, Integer> newSpeeds = new HashMap<>();
        Road road = map.getRoads().get(0);
        for (Car car : cars) {
            int speed = car.getCurrentSpeed();
            int maxSpeed = car.getMaxSpeed();
            int position = car.getPosition();

            // New Speed calculation
            speed = Math.min(speed + 1, maxSpeed); // Speed up

            if (speed > 0 && rand.nextDouble() < slowProbability) {
                speed -= 1; // Random speed change
            }
            else if (speed < maxSpeed && rand.nextDouble() < speedupProbability) {
                speed = Math.min(speed + 1, maxSpeed); // Random speed change
            }

            speed = Math.min(speed, map.gap(road, position)); // Break

            RoadObject ahead = map.getObjectAhead(road, position);
            if (ahead instanceof Obstacle obs && !obs.isOpen() && map.gap(road, position) == 0) {
                speed = 0;
            }

            newSpeeds.put(car, speed); // New final speed
        }

        return newSpeeds;
    }

    //Moves all the cars to their new positons and removes cars that leave the road
    private void moveCars(HashMap<Car, Integer> newSpeeds) {
        ArrayList<Car> carsToRemove = new ArrayList<>();
        Road road = map.getRoads().get(0);
        // Moves cars to new positions
        for (Car car : cars) {
            int oldPos = car.getPosition();
            int newSpeed = newSpeeds.get(car);
            int newPos = oldPos + newSpeed;

            // Remove from old cell
            if (road.getCell(oldPos) == car) {
                road.setCell(oldPos, null);

            }
            // If car exits road - Currently removes, can be changed to loop
            if (newPos >= road.getLength()) {
                carsToRemove.add(car); // Collects all cars that should be removed
                continue;
            }
            // Place in new cell
            if (road.getCell(newPos) instanceof Obstacle) {
                newPos++;
            }
            car.setPosition(newPos);
            car.setCurrentSpeed(newSpeed);
            road.setCell(newPos, car);
        }
        cars.removeAll(carsToRemove); // Removes cars outside road
    }

    // Function spawns cars based on spawnrate per tick.
    private void spawnCar() {
        Road road = map.getRoads().get(0);
        if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
            Car newCar = new Car(nextID, 0, rand.nextInt(2, 6));
            cars.add(newCar);
            road.setCell(0, newCar);
            nextID++;
            stats.incrementTotalCarsSpawned();
        }
    }

    // Tick update. Should be callable by controller
    public void tickUpdate() {
        tickCount++;
        map.updateObstacles(tickCount);
        moveCars(updateSpeeds());
        stats.update(cars);
        spawnCar();
    }


    public Statistics getStats() {
        return stats;
    }

    public void resetSimulation() {
        road = null;
        cars = null;
        this.initializeSimulation();
    }
}
