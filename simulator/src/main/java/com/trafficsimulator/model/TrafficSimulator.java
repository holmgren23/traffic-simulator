package com.trafficsimulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.trafficsimulator.utility.Constants;

/**
 * The core simulation model. Manages cars, the map, and
 * advances the simulation one tick at a time.
 */
public class TrafficSimulator {
    private ArrayList<Car> cars;
    private Map map;
    private int tickCount;
    private int minSpeed, maxSpeed, roadLength;
    private double spawnProbability;
    private int nextID;
    private Random rand;
    private Statistics stats;
    private double slowProbability = 0;
    private double speedupProbability = 0;
    private double turnProbability = 0.5;

    /**
     * Creates a simulation with the given settings and map type.
     * @param spawnProbability The likelyhood that a new car spawns every tick for every spawn location
     * @param roadLength Length of roads in the map
     * @param minSpeed Minimum speed a car can be assigned on spawn
     * @param maxSpeed Maximum speed a car can be assigned on spawn
     * @param mapType Name of the map layout to use
     */
    public TrafficSimulator(double spawnProbability, int roadLength, int minSpeed, int maxSpeed, String mapType) {
        this.rand = new Random();
        this.spawnProbability = spawnProbability;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.roadLength = roadLength;

        Map selectedMap = switch (mapType){
          case "Highway" -> Map.createHighway(roadLength);
          case "One way street" -> Map.createOneWay(roadLength);
          case "T Intersection" -> Map.createTIntersection(roadLength);
          case "Autobahn" -> Map.createAutobahn(roadLength);
          case "Intersection Loop" -> Map.createLoop(roadLength);
          default -> Map.createHighway(roadLength); 
        };

        this.initializeSimulation(selectedMap);
    }

    /**
     * Creates a simulation with a seeded random instance and a pre-built map.
     * Used for deterministic testing of random behaviour.
     * @param rand Seeded random instance
     * @param map Pre-built map to simulate on
     */
    public TrafficSimulator(Random rand, Map map) {
        this.spawnProbability = 0.5;
        this.minSpeed = 2;
        this.maxSpeed = 6;
        this.rand = rand;
        this.initializeSimulation(map);
    }

    /**
     * Sets up the shared state for both constructors.
     * @param map Map to run the simulation on
     */
    private void initializeSimulation(Map map) {
        this.map = map;
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
        stats = new Statistics(minSpeed, maxSpeed, spawnProbability, roadLength, map.getType());
    }

    /**
     * @return All cars currently active in the simulation
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    /**
     * @return Number of ticks elapsed since the simulation started or was last reset
     */
    public int getCurrentTick() {
        return tickCount;
    }

    /**
     * @return The probability to spawn a new car every tick, in each spawn location
     */
    public double getSpawnProbability() {
        return spawnProbability;
    }

    /**
     * @param spawnProbability New target spawn probability
     */
    public void setSpawnProbability(double spawnProbability) {
        this.spawnProbability = spawnProbability;
    }

    /**
     * @param map New map to use in the simulation
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @return The map currently used by the simulation
     */
    public Map getMap() {
        return map;
    }

    /**
     * Calculates new speeds for all cars based on their gap ahead,
     * road speed bonus, and random slow/speedup probabilities.
     * Does not move any car.
     * @return Map of each car to its calculated new speed
     */
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

    /**
     * Moves all cars to their new positions using the pre-calculated speeds.
     * Cars that reach the end of their road are transferred or removed.
     * @param newSpeeds Map of each car to its new speed from {@link #updateSpeeds()}
     */
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
                //Collision detected, stop car and put back
                car.setCurrentSpeed(0);
                road.setCell(car.getPosition(), car);
            }
        }

        cars.removeAll(carsToRemove);
    }

    /**
     * Iterates over all cars and attempts a lane change for each one
     * that is not on lane-change cooldown.
     */
    private void tryChangeLanes() {
        for (Car car : cars) {
            if (car.getLaneChangeCooldown() > 0) {
                car.setLaneChangeCooldown(car.getLaneChangeCooldown() - 1);
            } else {
                tryChangeLane(car);
            }
        }
    }

    /**
     * Evaluates whether a single car should merge back to its slow lane
     * or move to an overtake lane, then executes the change if safe.
     * @param car Car to evaluate
     */
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

    /**
     * Moves a car from one road to another at the same position
     * and starts its lane-change cooldown.
     * @param car Car performing the lane change
     * @param from Road the car is leaving
     * @param to Road the car is moving to
     * @param position Cell index where the lane change occurs
     * @param overtaking True if moving to an overtake lane, false if merging back
     */
    private void executeChangeLane(Car car, Road from, Road to, int position, boolean overtaking) {
        from.setCell(position, null);
        car.setRoad(to);
        to.setCell(position, car);
        car.setLaneChangeCooldown(5);
    }
    
    /**
     * Determines whether a car should spawn at a given cell this tick.
     * @param cell The road cell at spawn position 0
     * @return True if the cell is empty and a random roll passes the spawn chance
     */
    private boolean spawnCondition(RoadObject cell) {
        double randomValue = this.rand.nextDouble(0, 1);
        return cell == null && randomValue < this.spawnProbability;
    }

    /**
     * Attempts to spawn a new car at position 0 of each spawn road,
     * using a probability derived from the configured spawn rate.
     */
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

    /**
     * Checks all cars against mid-road obstacles such as exit lanes.
     * Cars that want to turn are transferred to the exit lane immediately.
     */
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

    /**
     * Advances the simulation by one tick. Runs obstacle updates,
     * lane changes, speed calculations, movement, mid-road obstacle
     * handling, spawning, and statistics in that order.
     */
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

    /**
     * @return The statistics object tracking the current simulation run
     */
    public Statistics getStats() {
        return stats;
    }

    /**
     * @return Formatted final statistics report for the completed simulation
     */
    public String getFinalStats() {
        return stats.getFinalReport();
    }

    /**
     * @return Formatted report of the simulation's initial settings
     */
    public String getSettingsReport() {
        return stats.getSettingsReport();
    }

    /**
     * Clears all cars from the map and resets the tick count,
     * car ID counter, and statistics to their initial state.
     */
    public void resetSimulation() {
        map.clearVehicle();
        cars = new ArrayList<>();
        tickCount = 0;
        nextID = 0;
        stats = new Statistics(minSpeed, maxSpeed, spawnProbability, roadLength, map.getType());
    }
}
