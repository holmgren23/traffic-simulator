package com.trafficsimulator.model;

import java.util.ArrayList;

import com.trafficsimulator.model.Road.Direction;

/**
 * Holds all roads in a simulation layout and provides
 * utility methods for querying gaps, transferring cars,
 * and updating obstacles. Also contains factory methods
 * for creating pre-configured map layouts.
 */
public class Map {
    private ArrayList<Road> roads;
    private MapType type;
    private static final int CELL_SIZE = 5;

    /**
     * Creates an empty map of the given type.
     * Roads must be added separately via {@link #addRoad(Road)}.
     * @param type The layout type this map represents
     */
    public Map(MapType type) {
        this.type = type;
        roads = new ArrayList<>();
    }

    /**
     * @param road Road to add to this map
     */
    public void addRoad(Road road) {
        roads.add(road);
    }

    /**
     * @return All roads in this map
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Places an obstacle on a road at the obstacle's own position.
     * @param obstacle Obstacle to add
     * @param road Road to place the obstacle on
     */
    public void addObstacle(Obstacle obstacle, Road road) {
        road.setObstacle(obstacle.getPosition(), obstacle);
    }

    /**
     * @return The layout type of this map
     */
    public MapType getType() {
        return type;
    }

    /**
     * Recalculates the open/closed state of every obstacle on every road.
     * @param tickCount Current simulation tick
     */
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

    /**
     * Returns the number of empty cells between the given position
     * and the next blocking object (vehicle or closed obstacle) ahead.
     * Returns {@link Integer#MAX_VALUE} if the road end is reached first.
     * @param road Road to check
     * @param position Cell position of the car asking for the gap
     * @return Number of free cells ahead, or MAX_VALUE if nothing blocks before the road end
     */
    public int gap(Road road, int position) {
        Car car = (road.getCell(position) instanceof Car c) ? c : null;
        int i = position + 1;
        // Find gap to the next car
        while (i < road.getLength()) {
            RoadObject vehicle = road.getCell(i);
            RoadObject obstacle = road.getObstacle(i);

            if (vehicle != null) {
                break; // Find vehicle onbject
            }
            if (obstacle instanceof Obstacle obs && !obs.isOpenFor(car)) {
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

    /**
     * Returns the number of empty cells between the given position
     * and the nearest vehicle behind it on the same road.
     * Returns {@link Integer#MAX_VALUE} if no vehicle is found.
     * @param road Road to check
     * @param position Cell position to check behind
     * @return Number of free cells behind, or MAX_VALUE if nothing is behind
     */
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

    /**
     * Returns the first blocking road object ahead of the given position.
     * @param road Road to check
     * @param position Cell position to look ahead from
     * @return The blocking {@link RoadObject} ahead, or null if the road end is reached first
     */
    public RoadObject getObjectAhead(Road road, int position) {
        int gapAhead = gap(road, position);
        if (gapAhead == Integer.MAX_VALUE) {
            return null;
        }
        return road.getCell(position + gapAhead + 1);
    }

    /**
     * Removes all vehicles from every cell on every road.
     */
    public void clearVehicle() {
        for (Road road : roads) {
            for (int i = 0; i < road.getLength(); i++) {
                road.setCell(i, null);
            }
        }
    }

    /**
     * @return The maximum x-coordinate used by any road, used to size the view
     */
    public int getMapWidth() {
        return roads.stream()
            .mapToInt(r -> Math.max(
                Math.max(r.getStartX(), r.getEndX()),
                Math.max(r.getStartX(), r.getEndX())))
            .max().orElse(800);
    }
    /**
     * @return The maximum y-coordinate used by any road, plus padding, used to size the view
     */
    public int getMapHeight() {
        return roads.stream()
            .mapToInt(r -> Math.max(r.getStartY(), r.getEndY()))
            .max().orElse(200) + 30;
    }

    /**
     * Transfers a car that has reached the end of its road to the
     * connected exit road, or removes it if no exit exists or the
     * target cell is occupied.
     * @param car Car that has reached the end of its road
     * @param fromRoad Road the car is leaving
     * @param carsToRemove List to add the car to if it should be removed from the simulation
     */
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

    /**
     * Transfers a car onto an exit road via a mid-road {@link ExitLane} obstacle.
     * If the target cell is occupied the car stays in place.
     * @param car Car that wants to exit
     * @param exit The exit lane the car is turning into
     */
    public void transferCar(Car car, ExitLane exit) {
        Road fromRoad = car.getRoad();
        Road targetRoad = exit.getExitRoad();
        int targetPos = exit.getExitPosition();

        fromRoad.setCell(car.getPosition(), null);

        if (targetRoad.getCell(targetPos) == null) {
            car.setPosition(targetPos);
            car.setRoad(targetRoad);
            targetRoad.setCell(targetPos, car);
        } else {
            fromRoad.setCell(car.getPosition(), car);
        }
    }

    /**
     * Creates a two-way highway with two lanes per direction.
     * The outer lane in each direction is the slow/spawn lane,
     * the inner lane is the overtake lane.
     * @param roadLength Number of cells per road
     * @return Configured highway map
     */
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

    /**
     * Creates a single one-way road with a traffic light at its midpoint.
     * @param roadLength Number of cells on the road
     * @return Configured one-way map
     */
    public static Map createOneWay(int roadLength) {
        Map map = new Map(MapType.ONE_WAY);
        int laneWidth = 8;

        int width = roadLength * CELL_SIZE;

        Road road = new Road(roadLength, Direction.EAST, 0, 50, width, 50, laneWidth);
        map.addRoad(road);
        map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2), road);
        return map;
    }

    /**
     * Creates a T-intersection where a vertical road merges into a horizontal road at its midpoint.
     * @param roadLength Number of cells for the main horizontal road
     * @return Configured T-intersection map
     */
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

    /**
     * Creates a two-way road with five lanes per direction.
     * Each lane has a speed bonus that increases toward the fast lane.
     * @param roadLength Number of cells per road
     * @return Configured autobahn map
     */
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

    /**
     * Creates four roads connected in a clockwise loop via intersections.
     * Has no exits, so cars circulate indefinitely and the map fills up over time.
     * @param roadLength Number of cells per road segment
     * @return Configured intersection loop map
     */
    public static Map createLoop(int roadLength) {
        Map map = new Map(MapType.INTERSECTION_LOOP);
        int laneWidth = 8;
        int width = roadLength * CELL_SIZE * 4 / 3;
        int height = width;

        int junctionLeft = width / 4;
        int junctionRight = width * 3 / 4;
        int junctionTop = height / 4;
        int junctionBottom = height * 3 / 4;

        //Horizontal roads spanning up to the side vertical roads
        Road topRoad = new Road(roadLength, Direction.EAST, 0, junctionTop, junctionRight, junctionTop, laneWidth);
        Road bottomRoad = new Road(roadLength, Direction.WEST, width, junctionBottom, junctionLeft, junctionBottom, laneWidth);

        //Vertical roads spanning up to the horizontal roads
        Road leftRoad = new Road(roadLength, Direction.NORTH, junctionLeft, height, junctionLeft, junctionTop, laneWidth);
        Road rightRoad = new Road(roadLength, Direction.SOUTH, junctionRight, 0, junctionRight, junctionBottom, laneWidth);

        //Exit cell of all roads
        int position = roadLength - 1;
        
        //Junction cells of roads
        int junctionCell = roadLength / 4;

        int clearance = 5;

        //Horizontal roads intersects the into the vertical roads
        map.addObstacle(new Intersection(position, rightRoad, junctionCell, clearance), topRoad);
        map.addObstacle(new Intersection(position, leftRoad, junctionCell, clearance), bottomRoad);

        //Vertical roads intersects the into the horizontal roads
        map.addObstacle(new Intersection(position, topRoad, junctionCell, clearance), leftRoad);
        map.addObstacle(new Intersection(position, bottomRoad, junctionCell, clearance), rightRoad);

        topRoad.setExit(rightRoad, roadLength / 3);
        rightRoad.setExit(bottomRoad, roadLength / 3);
        bottomRoad.setExit(leftRoad, roadLength / 3);
        leftRoad.setExit(topRoad, roadLength / 3);

        map.addRoad(topRoad);
        map.addRoad(bottomRoad);
        map.addRoad(leftRoad);
        map.addRoad(rightRoad);

        return map;
    }
}