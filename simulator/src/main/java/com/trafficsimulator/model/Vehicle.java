package com.trafficsimulator.model;

import javafx.scene.paint.Color;

public class Vehicle implements RoadObject {
    private int id;
    private int length;
    private int maxSpeed;
    private int currentSpeed;
    private int acceleration;
    private int deceleration;
    private int position;
    private Color color;
    private Road road;
    private int laneChangeCooldown = 0;
    private static final Color[] CAR_COLORS = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE
    };

    // Standard vehicle contructor
    public Vehicle(int id, int startPosition, int maxSpeed) {
        this.id = id;
        this.position = startPosition;
        this.length = 1;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = maxSpeed;
        this.color = CAR_COLORS[id % CAR_COLORS.length];
    }

    // Setters and Getters
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

    public Color getColor() {
        return color;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public int getLaneChangeCooldown() {
        return laneChangeCooldown;
    }

    public void setLaneChangeCooldown(int cooldown) {
        this.laneChangeCooldown = cooldown;
    }

}
