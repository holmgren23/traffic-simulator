package com.trafficsimulator.model;

import java.util.ArrayList;

import com.trafficsimulator.model.Road.Direction;

public class Map {
    private ArrayList<Road> roads;
    private MapType type;
    private static final int CELL_SIZE = 5;

    //initialize a new map that can hold roads
    public Map(MapType type) {
        this.type = type;
        roads = new ArrayList<>();
    }
    //Add a road to a map
    public void addRoad(Road road) {
        roads.add(road);
    }
    //Get roads from a map
    public ArrayList<Road> getRoads() {
        return roads;
    }
    //add an obstacle to a road based on the obstacles position
    public void addObstacle(Obstacle obstacle, Road road) {
        road.setObstacle(obstacle.getPosition(), obstacle);
    }

    //Returns preconfigured standard map types
    public MapType getType(){
        return type;
    }

    //Update all obstacles for all roads.
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

    //Finds and returns the distance to the Road Object ahead.
    public int gap(Road road, int position) {
        int i = position + 1;
        // Find gap to the next car
        while (i < road.getLength()) {
            RoadObject vehicle = road.getCell(i);
            RoadObject obstacle = road.getObstacle(i);
        
            if (vehicle != null) {
                break; //Find vehicle onbject
            }
            if (obstacle instanceof Obstacle obs && !obs.isOpen()) {
                break; //Find obstacle object
            }
            i++;
        }
        // If end of road, just go
        if (i >= road.getLength()) {
            return Integer.MAX_VALUE;
        }
        return i - position - 1;
    }

    //Gets the Road object that is ahead
    public RoadObject getObjectAhead(Road road, int position) {
        int gapAhead = gap(road, position);
        if (gapAhead == Integer.MAX_VALUE) {
            return null;
        }
        return road.getCell(position + gapAhead + 1);
    }

    //Clears all vehicles from the map
    public void clearVehicle() {
        for (Road road : roads) {
            for (int i = 0; i < road.getLength(); i++) {
                road.setCell(i, null);
            }
        }
    }

    //Fucntions for finding how much space the map needs in the window
    public int getMapWidth() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartX(), r.getEndX())).max().orElse(800);
    }
    public int getMapHeight() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartY(), r.getEndY())).max().orElse(100) + 30;
    }

    //For transfering cars at the end of a road to an other road object
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

    //Two way road constructor with traffic light on each side
    public static Map createHighway(int roadLength) {
        Map map = new Map(MapType.HIGHWAY);

        int laneWidth = 8;
        int width = roadLength * CELL_SIZE;

        map.addRoad(new Road(roadLength, Direction.EAST, 0 , 48, width, 48, laneWidth));
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 52, width, 52, laneWidth));
        map.addRoad(new Road(roadLength, Direction.EAST, 0, 44, width, 44, laneWidth));//lane 2 nedre
        map.addRoad(new Road(roadLength, Direction.WEST, 0, 56, width, 56, laneWidth));//lane 4 övre 


        //map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2), road1);
        //map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2 - 1), road2);

        return map;
    }

    //Creates a one way road
    public static Map createOneWay(int roadLength) {
        Map map = new Map(MapType.ONE_WAY);
        int laneWidth = 8;

        int width = roadLength * CELL_SIZE;

        Road road = new Road(roadLength, Direction.EAST, 0, 50, width, 50, laneWidth);
        map.addRoad(road);
        return map;
    }

    //Creates a map with a one road intersecting into an other road
    public static Map createTIntersection(int roadLength) {
        Map map = new Map(MapType.T_INTERSECTION);
        int width = roadLength * CELL_SIZE;
        int junctionX = width / 2;
        int laneWidth = 8;

        Road roadA = new Road(roadLength, Direction.EAST, 0, 70, width, 70, laneWidth);
        Road roadB = new Road(roadLength / 2, Direction.SOUTH, junctionX, 0, junctionX, 70, laneWidth);


        int junctionPoint = roadLength / 2;
        int clearance = 5;

        Intersection intersection = new Intersection(roadB.getLength() - 1, roadA, junctionPoint, clearance);
        roadB.setExit(roadA, junctionPoint);

        map.addRoad(roadA);
        map.addRoad(roadB);
        map.addObstacle(intersection, roadB);

        return map;
    }
}