package com.trafficsimulator.model;


public class Vehicle implements RoadObject {
    private int id;
    private int length;
    private int maxSpeed;
    private int currentSpeed;
    private int acceleration;
    private int deceleration;
    private int position;

    public Vehicle(int id, int startPosition, int maxSpeed) {
        this.id = id;
        this.position = startPosition;
        this.length = 1;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = maxSpeed;
    }

    public int getID() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int newMaxSpeed) {
        this.maxSpeed = newMaxSpeed;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int newCurrentSpeed) {
        this.currentSpeed = newCurrentSpeed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int newPosition) {
        position = newPosition;
    }
}
