package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.trafficsimulator.utility.Constants;

public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Map map;
    private int tickCount;
    private int spawnRate, minSpeed, maxSpeed, roadLength;
    private double spawnChance;
    private int nextID;
    private Random rand;
    private Statistics stats;
    private double slowProbability = 0;
    private double speedupProbability = 0;
    private double turnProbability = 0.5;

    // Main constructor
    public TrafficSimulator(int spawnRate, int roadLength, int minSpeed, int maxSpeed, String mapType) {
        this.rand = new Random();
        this.spawnRate = spawnRate;
        this.spawnChance = (spawnRate * 0.8) / Constants.MAX_TICKS_PER_SPAWN;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.roadLength = roadLength;

        Map selectedMap = switch (mapType){
          case "Highway" -> Map.createHighway(roadLength);
          case "One way street" -> Map.createOneWay(roadLength);
          case "T Intersection" -> Map.createTIntersection(roadLength);
          case "Autobahn" -> Map.createAutobahn(roadLength);
          default -> Map.createHighway(roadLength); 
        };

        this.initializeSimulation(selectedMap);
    }

    // Deterministic random constructor for testing of random functions.
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
        stats = new Statistics(minSpeed, maxSpeed, spawnRate, roadLength, map.getType());
    }

    // Getters
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

            // New Speed calculation
            int effectiveMaxSpeed = maxSpeed + road.getSpeedBonus();
            speed = Math.min(speed + 1, effectiveMaxSpeed); // Speed up

            if (speed > 0 && rand.nextDouble() < slowProbability) {
                speed -= 1; // Random speed change
            } else if (speed < effectiveMaxSpeed && rand.nextDouble() < speedupProbability) {
                speed = Math.min(speed + 1, effectiveMaxSpeed); // Random speed change
            }

            int gap = map.gap(road, position);
            speed = Math.min(speed, gap); // Brake

            RoadObject ahead = map.getObjectAhead(road, position);
            if (ahead instanceof Obstacle obs && !obs.isOpen() && gap == 0) {
                speed = 0;
            }

            newSpeeds.put(car, speed); // New final speed
        }

        return newSpeeds;
    }

    // Moves all the cars to their new positons and removes cars that leave the road
    private void moveCars(HashMap<Car, Integer> newSpeeds) {
        ArrayList<Car> carsToRemove = new ArrayList<>();
        HashMap<Car, Integer> resolvedPositions = new HashMap<>();

        //calculate all new positions and clear old cells
        for (Car car : cars) {
            Road road = car.getRoad();
            int oldPos = car.getPosition();
            int newSpeed = newSpeeds.get(car);
            int newPos = oldPos + newSpeed;

            if (road.getCell(oldPos) == car) {
                road.setCell(oldPos, null);
            }

            if (newPos >= road.getLength()) {
                resolvedPositions.put(car, -1); // mark for transfer
            } else {
                resolvedPositions.put(car, newPos);
            }
        }

        //place all cars in new positions
        for (Car car : cars) {
            Road road = car.getRoad();
            int newPos = resolvedPositions.get(car);
            int newSpeed = newSpeeds.get(car);

            if (newPos == -1) {
                map.transferCar(car, road, carsToRemove);
                continue;
            }

            if (road.getCell(newPos) == null) {
                car.setPosition(newPos);
                car.setCurrentSpeed(newSpeed);
                road.setCell(newPos, car);
            } else {
                // Collision detected — stop car and put back
                car.setCurrentSpeed(0);
                road.setCell(car.getPosition(), car);
            }
        }

        cars.removeAll(carsToRemove);
    }

    // Function to overtake
    private void tryChangeLanes() {
        for (Car car : cars) {
            if (car.getLaneChangeCooldown() > 0) {
                car.setLaneChangeCooldown(car.getLaneChangeCooldown() - 1);
            } else {
                tryChangeLane(car);
            }
        }
    }

    private void tryChangeLane(Car car) {
        Road currentRoad = car.getRoad();
        int position = car.getPosition();
        int speed = car.getCurrentSpeed();
        int gapAheadCurrent = map.gap(currentRoad, position);

        // Prefer merging back toward the slow/spawn lane when safe
        Road mergeLane = currentRoad.getMergeLane();
        if (mergeLane != null) {
            int gapAhead = map.gap(mergeLane, position);
            int gapBehind = map.gapBehind(mergeLane, position);
            int speedBehind = 0;
            if (gapBehind != Integer.MAX_VALUE) {
                RoadObject behind = mergeLane.getCell(position - gapBehind - 1);
                speedBehind = (behind instanceof Car c) ? c.getCurrentSpeed() : 0;
            }
            if (gapAhead >= speed && gapBehind > speedBehind) {
                executeChangeLane(car, currentRoad, mergeLane, position, false);
                return;
            }
        }

        // Move to the faster/overtake lane when blocked and it's better
        Road overtakeLane = currentRoad.getOvertakeLane();
        if (overtakeLane != null) {
            int gapAhead = map.gap(overtakeLane, position);
            int gapBehind = map.gapBehind(overtakeLane, position);
            int speedBehind = 0;
            if (gapBehind != Integer.MAX_VALUE) {
                RoadObject behind = overtakeLane.getCell(position - gapBehind - 1);
                speedBehind = (behind instanceof Car c) ? c.getCurrentSpeed() : 0;
            }
            boolean blocked = gapAheadCurrent < car.getMaxSpeed();
            boolean betterAhead = gapAhead > gapAheadCurrent;
            if (blocked && betterAhead && gapAhead > 0 && gapBehind > speedBehind) {
                executeChangeLane(car, currentRoad, overtakeLane, position, true);
            }
        }
    }

    private void executeChangeLane(Car car, Road from, Road to, int position, boolean overtaking) {
        from.setCell(position, null);
        car.setRoad(to);
        to.setCell(position, car);
        car.setLaneChangeCooldown(5);
    }
    
    private boolean spawnCondition(RoadObject cell) {
        double randomValue = this.rand.nextDouble(0, 1);
        return cell == null && randomValue < this.spawnChance;
    }

    // Function spawns cars based on spawnrate per tick.
    private void spawnCar() {
        for (Road road : map.getRoads()) {
            if (!road.isSpawnRoad())
                continue;
            if (this.spawnCondition(road.getCell(0))) {
                Car newCar = new Car(nextID, 0, rand.nextInt(this.minSpeed, this.maxSpeed + 1), turnProbability);
                newCar.setRoad(road);
                cars.add(newCar);
                road.setCell(0, newCar);
                nextID++;
            }
        }
    }

    private void handleMidRoadObstacles() {
        for (Car car : cars) {
            Road road = car.getRoad();
            RoadObject obstacle = road.getObstacle(car.getPosition());
            if (obstacle instanceof ExitLane exit && car.wantsToTurn()) {
                car.setCurrentSpeed(0);
                map.transferCar(car, exit);
            }
        }
    }

    // Tick update. Should be callable by controller
    public void tickUpdate() {
        tickCount++;
        map.updateObstacles(tickCount);
        tryChangeLanes();
        HashMap<Car, Integer> newSpeeds = updateSpeeds();
        moveCars(newSpeeds);
        handleMidRoadObstacles();
        spawnCar();
        stats.update(cars, nextID);
    }

    // Returns the statistics of the current tick
    public Statistics getStats() {
        return stats;
    }

    // Gets the final state of statistics for the termination of the simulation
    public String getFinalStats() {
        return stats.getFinalReport();
    }

    public String getSettingsReport() {
        return stats.getSettingsReport();
    }

    // Clears all cars of the map. Resets Cars, tickcount, car ID and Statistic
    public void resetSimulation() {
        map.clearVehicle();
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
        stats = new Statistics(minSpeed, maxSpeed, spawnRate, roadLength, map.getType());
    }
}
