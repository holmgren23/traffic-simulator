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
    private Statistics stats;
    private ArrayList<Obstacle> obstacles;
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
        obstacles = new ArrayList<>();
    
    }

    //Getters
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

    // Adds an obbstacle to the list of obstacles. Should maybe be moved somewhere else
    public void addObstacle(Obstacle obstacle) {
        obstacles.add(obstacle);
        road.setObstacle(obstacle.getPosition(), obstacle);
    }

    // HELPER: Finds gap from car to the car ahead
    public int gap(int position) {
        int i = position + 1;
        // Find gap to the next car
        while (i < road.getLength()) {
            RoadObject vehicle = road.getCell(i);
            RoadObject obstacle = road.getObstacle(i);
        
            if (vehicle != null) {
                break;
            }
            if (obstacle instanceof Obstacle obs && !obs.isOpen()) {
                break;
            }
            i++;
        }
        // If end of road, just go
        if (i >= road.getLength()) {
            return Integer.MAX_VALUE;
        }
        return i - position - 1;
    }

    // HELPER: gets the object that is ahead
    private RoadObject getObjectAhead(int position) {
        int gapAhead = gap(position);
        if (gapAhead == Integer.MAX_VALUE) {
            return null;
        }
        return road.getCell(position + gapAhead + 1);
    }

    // Calculates Positions for cars, Does not move
    private HashMap<Car, Integer> updateSpeeds() {
        HashMap<Car, Integer> newSpeeds = new HashMap<>();

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

            speed = Math.min(speed, gap(position)); // Break

            RoadObject ahead = getObjectAhead(position);
            if (ahead instanceof Obstacle obs && !obs.isOpen() && gap(position) == 0) {
                speed = 0;
            }

            newSpeeds.put(car, speed); // New final speed
        }

        return newSpeeds;
    }

    //Moves all the cars to their new positons and removes cars that leave the road
    private void moveCars(HashMap<Car, Integer> newSpeeds) {
        ArrayList<Car> carsToRemove = new ArrayList<>();
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
    private void spawnCar(Road road) {
        if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
            Car newCar = new Car(nextID, 0, rand.nextInt(2, 6));
            cars.add(newCar);
            road.setCell(0, newCar);
            nextID++;
            stats.incrementTotalCarsSpawned();
        }
    }

    // Updates all the states for all obstacles
    private void updateObstacles() {
        for (Obstacle obstacle : obstacles) {
            obstacle.calculatePriority(tickCount);
        }
    }

    // Tick update. Should be callable by controller
    public void tickUpdate() {
        tickCount++;
        updateObstacles();
        moveCars(updateSpeeds());
        stats.update(cars);
        spawnCar(road);
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
