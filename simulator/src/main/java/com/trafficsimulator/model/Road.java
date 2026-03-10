package com.trafficsimulator.model;

public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;
    private Direction direction;
    private int startX, startY;
    private int endX, endY;
    public enum Direction {
        EAST, WEST, NORTH, SOUTH
    }

    //Road that can take a vehicle and obstacle in the same cell but different layers(arrays)
    public Road(int length, Direction direction, int startX, int startY, int endX, int endY) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    //Default contructor
    public Road(int length, Direction direction) {
        this(length, Direction.EAST, 0 , 0, 0, 0);
    }

    public Road(int length) {
        this(length, Direction.EAST, 0, 0, 0,0);
    }

    //Gets road length
    public int getLength() {
        return vehicles.length;
    }
    //Get road direction
    public Direction getDirection() {
        return direction;
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
}