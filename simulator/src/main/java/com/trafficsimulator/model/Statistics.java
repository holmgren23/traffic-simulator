package com.trafficsimulator.model;

import java.util.ArrayList;

public class Statistics {
    private int totalCarsSpawned = 0;
    private double currentAverageSpeed = 0;
    private int currentStationaryCars = 0;
    private int currentAmountOfCars = 0;


    public void incrementTotalCarsSpawned() {
        this.totalCarsSpawned++;
    }

    public void update(ArrayList<Car> cars) {
        if (cars.isEmpty()) {
            this.currentAverageSpeed = 0;
            this.currentStationaryCars = 0;
            return;
        }

        double sumSpeed = 0;
        int stoppedCount = 0;

        for (Car car : cars) {
            sumSpeed += car.getCurrentSpeed();
            if (car.getCurrentSpeed() == 0) {
                stoppedCount++;
            }
        }

        this.currentAmountOfCars = cars.size();
        this.currentAverageSpeed = sumSpeed / cars.size();
        this.currentStationaryCars = stoppedCount;
    }

    public int getTotalCarsSpawned() {
        return totalCarsSpawned;
    }

    public double getAverageSpeed() {
        return currentAverageSpeed;
    }

    public int getStationaryCars() {
        return currentStationaryCars;
    }
    public int getCurrentAmountOfCars() {
        return currentAmountOfCars;
    }

}