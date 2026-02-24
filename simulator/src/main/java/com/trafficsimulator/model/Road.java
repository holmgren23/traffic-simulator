package com.trafficsimulator.model;

public class Road {
    private RoadObject[] road;

    public Road(int length) {
        road = new RoadObject[length];
    }

    public int getLength() {
        return road.length;
    }

    public RoadObject getCell(int index) {
        return road[index];
    }

    public void setCell(int index, RoadObject roadObject) {
        road[index] = roadObject;
    }
}
