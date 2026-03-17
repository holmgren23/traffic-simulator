package com.trafficsimulator.model;

import java.util.Random;

import javafx.scene.paint.Color;

/**
 * Base class for all vehicles in the simulation.
 * Holds position, speed, road, lane-change state, and turn intent.
 */
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

    /**
     * Creates a vehicle with the given settings.
     * The vehicle's color is assigned by cycling through a fixed palette using the ID.
     * @param id Unique identifier for the vehicle
     * @param startPosition Starting cell position on the road
     * @param maxSpeed Maximum speed the vehicle can travel
     * @param turnProbability Probability (0–1) that the vehicle will want to turn at an intersection
     */
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

    /**
     * @return Unique identifier of the vehicle
     */
    public int getID() {
        return id;
    }

    /**
     * @return Length of the vehicle in cells
     */
    public int getLength() {
        return length;
    }

    /**
     * @return Maximum speed the vehicle can travel
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param newMaxSpeed New maximum speed for the vehicle
     */
    public void setMaxSpeed(int newMaxSpeed) {
        this.maxSpeed = newMaxSpeed;
    }

    /**
     * @return Current speed of the vehicle this tick
     */
    public int getCurrentSpeed() {
        return currentSpeed;
    }

    /**
     * @param newCurrentSpeed New current speed for the vehicle
     */
    public void setCurrentSpeed(int newCurrentSpeed) {
        this.currentSpeed = newCurrentSpeed;
    }

    /**
     * @return Current cell position of the vehicle on its road
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param newPosition New cell position on the road
     */
    public void setPosition(int newPosition) {
        position = newPosition;
    }

    /**
     * @return Display color assigned to this vehicle
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return The road the vehicle is currently on
     */
    public Road getRoad() {
        return road;
    }

    /**
     * @param road Road to place the vehicle on
     */
    public void setRoad(Road road) {
        this.road = road;
    }

    /**
     * @return Remaining ticks before the vehicle may change lanes again
     */
    public int getLaneChangeCooldown() {
        return laneChangeCooldown;
    }

    /**
     * @param cooldown Number of ticks to wait before the next lane change is allowed
     */
    public void setLaneChangeCooldown(int cooldown) {
        this.laneChangeCooldown = cooldown;
    }

    /**
     * Recalculates whether this vehicle wants to turn at the next opportunity.
     * The decision is re-evaluated every 10 ticks using the turn probability.
     * @param rand Random instance used for the probability roll
     * @param tickCount Current simulation tick, used to track cooldown
     */
    public void updateTurnIntent(Random rand, int tickCount) {
        if (turnIntentCooldown <= 0) {
            wantsToTurn = rand.nextDouble() < turnProbability;
            turnIntentCooldown = 10; //recalculate intent
        } else {
            turnIntentCooldown--;
        }
    }

    /**
     * @return True if the vehicle intends to turn at the next available exit or intersection
     */
    public boolean wantsToTurn() {
        return wantsToTurn; 
    }
}
