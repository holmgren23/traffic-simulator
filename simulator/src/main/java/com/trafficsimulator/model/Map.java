package com.trafficsimulator.model;

import java.util.ArrayList;

import com.trafficsimulator.model.Road.Direction;

public class Map {
    private ArrayList<Road> roads;
    private MapType type;
    private static final int CELL_SIZE = 5;

    // initialize a new map that can hold roads
    public Map(MapType type) {
        this.type = type;
        roads = new ArrayList<>();
    }

    // Add a road to a map
    public void addRoad(Road road) {
        roads.add(road);
    }

    // Get roads from a map
    public ArrayList<Road> getRoads() {
        return roads;
    }

    // add an obstacle to a road based on the obstacles position
    public void addObstacle(Obstacle obstacle, Road road) {
        road.setObstacle(obstacle.getPosition(), obstacle);
    }

    // Returns preconfigured standard map types
    public MapType getType() {
        return type;
    }

    // Update all obstacles for all roads.
    public void updateObstacles(int tickCount) {
        for (Road road : roads) {
            for (int i = 0; i < road.getLength(); i++) {
                RoadObject obj = road.getObstacle(i);
                if (obj instanceof Obstacle obs) {
                    obs.calculatePriority(tickCount);
                }
            }
        }
    }

    // Finds and returns the distance to the Road Object ahead.
    public int gap(Road road, int position) {
        int i = position + 1;
        // Find gap to the next car
        while (i < road.getLength()) {
            RoadObject vehicle = road.getCell(i);
            RoadObject obstacle = road.getObstacle(i);

            if (vehicle != null) {
                break; // Find vehicle onbject
            }
            if (obstacle instanceof Obstacle obs && !obs.isOpen()) {
                break; // Find obstacle object
            }
            i++;
        }
        // If end of road, just go
        if (i >= road.getLength()) {
            return Integer.MAX_VALUE;
        }
        return i - position - 1;
    }

    public int gapBehind(Road road, int position) {
        int i = position - 1;
        while (i >= 0) {
            RoadObject vehicle = road.getCell(i);
            if (vehicle != null) {
                break;
            }
            i--;
        }
        if (i < 0) {
            return Integer.MAX_VALUE;
        }
        return position - i - 1;
    }

    // Gets the Road object that is ahead
    public RoadObject getObjectAhead(Road road, int position) {
        int gapAhead = gap(road, position);
        if (gapAhead == Integer.MAX_VALUE) {
            return null;
        }
        return road.getCell(position + gapAhead + 1);
    }

    // Clears all vehicles from the map
    public void clearVehicle() {
        for (Road road : roads) {
            for (int i = 0; i < road.getLength(); i++) {
                road.setCell(i, null);
            }
        }
    }

    // Fucntions for finding how much space the map needs in the window
    public int getMapWidth() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartX(), r.getEndX())).max().orElse(800);
    }

    public int getMapHeight() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartY(), r.getEndY())).max().orElse(100) + 30;
    }

    // For transfering cars at the end of a road to an other road object
    public void transferCar(Car car, Road fromRoad, ArrayList<Car> carsToRemove) {
        fromRoad.setCell(car.getPosition(), null);

        // default — remove car
        if (!fromRoad.hasExit()) {
            carsToRemove.add(car);
            return;
        }

        Road targetRoad = fromRoad.getExitRoad();
        int targetPos = fromRoad.getExitPosition();

        if (targetRoad.getCell(targetPos) == null) {
            car.setPosition(targetPos);
            car.setRoad(targetRoad);
            targetRoad.setCell(targetPos, car);
        } else {
            carsToRemove.add(car);
        }
    }

    // Two way road constructor with traffic light on each side
    public static Map createHighway(int roadLength) {
        Map map = new Map(MapType.HIGHWAY);

        int laneWidth = 8;
        int width = roadLength * CELL_SIZE;

        map.addRoad(new Road(roadLength, Direction.WEST, 0, 44, width, 44, laneWidth));
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 48, width, 48, laneWidth));
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 52, width, 52, laneWidth));
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 56, width, 56, laneWidth));

        map.getRoads().get(0).setOvertakeLane(map.getRoads().get(1));
        map.getRoads().get(1).setMergeLane(map.getRoads().get(0));

        map.getRoads().get(3).setOvertakeLane(map.getRoads().get(2));
        map.getRoads().get(2).setMergeLane(map.getRoads().get(3));

        map.getRoads().get(1).setSpawnRoad(false);
        map.getRoads().get(2).setSpawnRoad(false);

        return map;
    }

    // Creates a one way road
    public static Map createOneWay(int roadLength) {
        Map map = new Map(MapType.ONE_WAY);
        int laneWidth = 8;

        int width = roadLength * CELL_SIZE;

        Road road = new Road(roadLength, Direction.EAST, 0, 50, width, 50, laneWidth);
        map.addRoad(road);
        map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2), road);
        return map;
    }

    // Creates a map with a one road intersecting into an other road
    public static Map createTIntersection(int roadLength) {
        Map map = new Map(MapType.T_INTERSECTION);
        int width = roadLength * CELL_SIZE;
        int junctionX = width / 2;
        int laneWidth = 8;

        Road roadA = new Road(roadLength, Direction.EAST, 0, 70, width, 70, laneWidth);
        Road roadB = new Road(roadLength / 2, Direction.SOUTH, junctionX, 0, junctionX, 70 - laneWidth / 2, laneWidth);

        int junctionPoint = roadLength / 2;
        int clearance = 5;

        Intersection intersection = new Intersection(roadB.getLength() - 1, roadA, junctionPoint, clearance);
        roadB.setExit(roadA, junctionPoint);

        map.addRoad(roadA);
        map.addRoad(roadB);
        map.addObstacle(intersection, roadB);

        return map;
    }

    public static Map createAutobahn(int roadLength) {
        Map map = new Map(MapType.AUTOBAHN);

        int laneWidth = 8;
        int width = roadLength * CELL_SIZE;

        // Westbound: lanes 0-4, Y=28..44 (0=slow/spawn, 4=fast)
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 28, width, 28, laneWidth)); // 0 spawn
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 32, width, 32, laneWidth)); // 1
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 36, width, 36, laneWidth)); // 2
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 40, width, 40, laneWidth)); // 3
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 44, width, 44, laneWidth)); // 4 fast

        // Eastbound: lanes 5-9, Y=48..64 (9=slow/spawn, 5=fast)
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 48, width, 48, laneWidth)); // 5 fast
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 52, width, 52, laneWidth)); // 6
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 56, width, 56, laneWidth)); // 7
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 60, width, 60, laneWidth)); // 8
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 64, width, 64, laneWidth)); // 9 spawn

        // Westbound overtake chain: 0 -> 1 -> 2 -> 3 -> 4
        map.getRoads().get(0).setOvertakeLane(map.getRoads().get(1));
        map.getRoads().get(1).setOvertakeLane(map.getRoads().get(2));
        map.getRoads().get(2).setOvertakeLane(map.getRoads().get(3));
        map.getRoads().get(3).setOvertakeLane(map.getRoads().get(4));
        // Westbound merge chain: 4 -> 3 -> 2 -> 1 -> 0
        map.getRoads().get(4).setMergeLane(map.getRoads().get(3));
        map.getRoads().get(3).setMergeLane(map.getRoads().get(2));
        map.getRoads().get(2).setMergeLane(map.getRoads().get(1));
        map.getRoads().get(1).setMergeLane(map.getRoads().get(0));

        // Eastbound overtake chain: 9 -> 8 -> 7 -> 6 -> 5
        map.getRoads().get(9).setOvertakeLane(map.getRoads().get(8));
        map.getRoads().get(8).setOvertakeLane(map.getRoads().get(7));
        map.getRoads().get(7).setOvertakeLane(map.getRoads().get(6));
        map.getRoads().get(6).setOvertakeLane(map.getRoads().get(5));
        // Eastbound merge chain: 5 -> 6 -> 7 -> 8 -> 9
        map.getRoads().get(5).setMergeLane(map.getRoads().get(6));
        map.getRoads().get(6).setMergeLane(map.getRoads().get(7));
        map.getRoads().get(7).setMergeLane(map.getRoads().get(8));
        map.getRoads().get(8).setMergeLane(map.getRoads().get(9));

        // Westbound: lane 0 = slow (+0), lane 4 = fast (+4)
        for (int i = 0; i <= 4; i++) {
            map.getRoads().get(i).setSpeedBonus(i);
        }
        // Eastbound: lane 9 = slow (+0), lane 5 = fast (+4)
        for (int i = 5; i <= 9; i++) {
            map.getRoads().get(i).setSpeedBonus(9 - i);
        }

        return map;
    }
}