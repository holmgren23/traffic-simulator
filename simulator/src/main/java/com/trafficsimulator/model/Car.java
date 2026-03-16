package com.trafficsimulator.model;

/**
 * A car that can travel on roads, change lanes, and turn at intersections.
 * Extends {@link Vehicle} with no additional behaviour currently.
 */
public class Car extends Vehicle {

    /**
     * Creates a car with the given settings.
     * @param id Unique identifier for the car
     * @param startPosition Starting cell position on the road
     * @param maxSpeed Maximum speed the car can travel
     * @param turnProbability Probability (0–1) that the car will want to turn at an intersection
     */
    public Car(int id, int startPosition, int maxSpeed, double turnProbability) {
        super(id, startPosition, maxSpeed, turnProbability);
    }
}
