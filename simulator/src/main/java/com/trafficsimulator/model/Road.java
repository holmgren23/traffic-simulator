package com.trafficsimulator.model;

public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;
    private Direction direction;
    private int startX, startY;
    private int endX, endY;
    private Road exitRoad;
    private int exitPosition;
    private int laneWidth;
    private Road neighborLane;
    private boolean isSpawnRoad = true;
    public enum Direction {
        EAST, WEST, NORTH, SOUTH
    }

    //Road that can take a vehicle and obstacle in the same cell but different layers(arrays)
    public Road(int length, Direction direction, int startX, int startY, int endX, int endY, int laneWidth) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.laneWidth = laneWidth;
    }

    //Default contructor
    public Road(int length, Direction direction) {
        this(length, Direction.EAST, 0 , 0, 0, 0, 4);
    }

    public Road(int length) {
        this(length, Direction.EAST, 0, 0, 0,0, 4);
    }

    //Gets road length
    public int getLength() {
        return vehicles.length;
    }
    //Get road direction
    public Direction getDirection() {
        return direction;
    }

    //Width of lanes
    public int getLaneWidth() {
        return laneWidth;
    }

    //Sets the road that cars leaving the current road will transfer to
    public void setExit(Road exitRoad, int exitPosition) {
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    //Gets the road that cars will be transfered to at end of road
    public Road getExitRoad() {
        return exitRoad;
    }

    //Gets the positon that cars will appear on the road the cars are transfered to at end of road 
    public int getExitPosition() {
        return exitPosition;
    }

    //Returns if a road has a different road cars will be transfered to at end of road.
    //Sent to carsToRemove if null(default)
    public boolean hasExit() {
        return exitRoad != null;
    }

    public int getStartX() {return startX;}
    public int getStartY() {return startY;}
    public int getEndX() {return endX;}
    public int getEndY() {return endY;}

    // Vehicle layer
    public RoadObject getCell(int index) {
        return vehicles[index];
    }
    public void setCell(int index, RoadObject obj) {
        vehicles[index] = obj;
    }

    // Obstacle layer
    public RoadObject getObstacle(int index) {
        return obstacles[index];
    }
    public void setObstacle(int index, RoadObject obj) {
        obstacles[index] = obj;
    }

    //Overtake lane
    public void setNeighborLane(Road neighbor){
        this.neighborLane = neighbor;
    }

    public Road getNeighborLane(){
        return neighborLane;
    }

    public boolean isSpawnRoad(){
        return isSpawnRoad;
    }

    public void setSpawnRoad(boolean isSpawnRoad){
        this.isSpawnRoad = isSpawnRoad;
    }
}