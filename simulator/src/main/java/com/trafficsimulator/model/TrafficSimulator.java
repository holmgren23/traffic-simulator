package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Map map;
    private int tickCount;
    private int spawnRate, minSpeed, maxSpeed;
    private int nextID;
    private Random rand;
    private Statistics stats;
    private double slowProbability = 0.2;
    private double speedupProbability = 0.3;

    //Main constructor   
    public TrafficSimulator(int spawnRate, int roadLength, int minSpeed, int maxSpeed, Map map) {
        this.rand = new Random();
        this.spawnRate = spawnRate;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;

        this.initializeSimulation(map);
    }

    //Deterministic random constructor for testing of random functions.
    public TrafficSimulator(Random rand, Map map) {
        this.spawnRate = 5;
        this.minSpeed = 2;
        this.maxSpeed = 6;
        this.rand = rand;
        this.initializeSimulation(map);
    }
    
    private void initializeSimulation(Map map) {
        this.map = map;
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
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
    
    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    // Calculates Positions for cars, Does not move
    private HashMap<Car, Integer> updateSpeeds() {
        HashMap<Car, Integer> newSpeeds = new HashMap<>();

        for (Car car : cars) {
            Road road = car.getRoad();
            int speed = car.getCurrentSpeed();
            int maxSpeed = car.getMaxSpeed();
            int position = car.getPosition();

            //New Speed calculation
            speed = Math.min(speed + 1, maxSpeed); //Speed up

            if (speed > 0 && rand.nextDouble() < slowProbability) {
                speed -= 1; //Random speed change
            }
            else if (speed < maxSpeed && rand.nextDouble() < speedupProbability) {
                speed = Math.min(speed + 1, maxSpeed); //Random speed change
            }

            speed = Math.min(speed, map.gap(road, position)); //Break

            RoadObject ahead = map.getObjectAhead(road, position);
            if (ahead instanceof Obstacle obs && !obs.isOpen() && map.gap(road, position) == 0) {
                speed = 0;
            }

            newSpeeds.put(car, speed); //New final speed
        }

        return newSpeeds;
    }

    //Moves all the cars to their new positons and removes cars that leave the road
    private void moveCars(HashMap<Car, Integer> newSpeeds) {
        ArrayList<Car> carsToRemove = new ArrayList<>();
        
        //Moves cars to new positions
        for (Car car : cars) {
            Road road = car.getRoad();
            int oldPos = car.getPosition();
            int newSpeed = newSpeeds.get(car);
            int newPos = oldPos + newSpeed;

            //Remove from old cell
            if (road.getCell(oldPos) == car) {
                road.setCell(oldPos, null);

            }
            //If car exits road - Currently removes, can be changed to loop
            if (newPos >= road.getLength()) {
                carsToRemove.add(car); //Collects all cars that should be removed
                continue;
            }
            //Place in new cell
            car.setPosition(newPos);
            car.setCurrentSpeed(newSpeed);
            road.setCell(newPos, car);
        }
        cars.removeAll(carsToRemove); //Removes cars outside road
    }

    //Function spawns cars based on spawnrate per tick.
    private void spawnCar() {
        for (Road road : map.getRoads()) {
            if (tickCount % spawnRate == 0 && road.getCell(0) == null) {
                Car newCar = new Car(nextID, 0, rand.nextInt(this.minSpeed, this.maxSpeed + 1));
                newCar.setRoad(road);
                cars.add(newCar);
                road.setCell(0, newCar);
                nextID++;
            }
        }
    }

    //Tick update. Should be callable by controller
    public void tickUpdate() {
        tickCount++;
        map.updateObstacles(tickCount);
        moveCars(updateSpeeds());
        spawnCar();
        stats.update(cars, nextID);
    }

    //Returns the statistics of the current tick
    public Statistics getStats() {
        return stats;
    }
    
    public String getFinalStats() {
        return stats.getFinalReport();
    }

    //Clears all cars of the map. Resets Cars, tickcount, car ID and Statistic
    public void resetSimulation() {
        map.clearVehicle();
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
        stats = new Statistics();
    }
}
