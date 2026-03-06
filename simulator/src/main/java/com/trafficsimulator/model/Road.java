package com.trafficsimulator.model;

public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;
    private Direction direction;
    public enum Direction {
        EAST, WEST, NORTH, SOUTH
    }

    //Road that can take a vehicle and obstacle in the same cell but different layers(arrays)
    public Road(int length, Direction direction) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
        this.direction = direction;
    }

    //Default contructor
    public Road(int length) {
        this(length, Direction.EAST);
    }

    //Gets road length
    public int getLength() {
        return vehicles.length;
    }
    //Get road direction
    public Direction getDirection() {
        return direction;
    }

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