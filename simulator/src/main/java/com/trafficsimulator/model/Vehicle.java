package com.trafficsimulator.model;

import java.util.Random;

import javafx.scene.paint.Color;

public class Vehicle implements RoadObject {
    private int id;
    private int length;
    private int maxSpeed;
    private int currentSpeed;
    private int position;
    private Color color;
    private Road road;
    private boolean wantsToTurn;
    private int turnIntentCooldown;
    private double turnProbability;
    private int laneChangeCooldown;
    private static final Color[] CAR_COLORS = {
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE
    };

    // Standard vehicle contructor
    public Vehicle(int id, int startPosition, int maxSpeed, double turnProbability) {
        this.id = id;
        this.position = startPosition;
        this.length = 1;
        this.maxSpeed = maxSpeed;
        this.currentSpeed = maxSpeed;
        this.color = CAR_COLORS[id % CAR_COLORS.length];
        this.turnProbability = turnProbability;
        this.laneChangeCooldown = 0;
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

    //Update conditions for vehicles intent to turn at next turn option.
    //Currently a random probability calculate every couple of ticks
    public void updateTurnIntent(Random rand, int tickCount) {
        if (turnIntentCooldown <= 0) {
            wantsToTurn = rand.nextDouble() < turnProbability;
            turnIntentCooldown = 10; //recalculate intent
        } else {
            turnIntentCooldown--;
        }
    }

    //Vechiles current intent to turn
    public boolean wantsToTurn() { 
        return wantsToTurn; 
    }
}
