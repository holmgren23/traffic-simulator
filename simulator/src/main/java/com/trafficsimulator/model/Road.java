package com.trafficsimulator.model;

public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;

    //Road that can take a vehicle and obstacle in the same cell but different layers(arrays)
    public Road(int length) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
    }

    //Gets road length
    public int getLength() {
        return vehicles.length;
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