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

    public int getMapWidth() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartX(), r.getEndX())).max().orElse(800);
    }

    public int getMapHeight() {
        return roads.stream().mapToInt(r -> Math.max(r.getStartY(), r.getEndY())).max().orElse(100) + 30;
    }

    //Two way road constructor with traffic light on each side
    public static Map createHighway(int roadLength) {
        Map map = new Map(MapType.HIGHWAY);

        int width = roadLength * CELL_SIZE;

        Road road1 = new Road(roadLength, Direction.EAST, 0 , 40, width, 40);
        Road road2 = new Road(roadLength, Direction.WEST, 0, 50, width, 50);

        map.addRoad(road1);
        map.addRoad(road2);
        map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2), road1);
        map.addObstacle(new Obstacle(ObstacleType.TRAFFIC_LIGHT, roadLength / 2 - 1), road2);

        return map;
    }

    //Creates a one way road
    public static Map createOneWay(int roadLength) {
        Map map = new Map(MapType.ONE_WAY);

        int width = roadLength * CELL_SIZE;

        Road road = new Road(roadLength, Direction.EAST, 0, 50, width, 50);
        map.addRoad(road);
        return map;
    }

    public static Map createTIntersection(int roadLength) {
        Map map = new Map(MapType.T_INTERSECTION);
        int width = roadLength * CELL_SIZE;
        int junctionX = width / 2;

        Road roadA = new Road(roadLength, Direction.EAST, 0, 70, width, 70);
        Road roadB = new Road(roadLength / 2, Direction.SOUTH, junctionX, 0, junctionX, 70);


        int junctionPoint = roadLength / 2;
        int clearance = 10;

        Intersection intersection = new Intersection(roadB.getLength() - 1, roadA, junctionPoint, clearance);

        map.addRoad(roadA);
        map.addRoad(roadB);
        map.addObstacle(intersection, roadB);

        return map;
    }
}