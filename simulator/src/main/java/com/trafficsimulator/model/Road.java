package com.trafficsimulator.model;

public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;

    public Road(int length) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
    }

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