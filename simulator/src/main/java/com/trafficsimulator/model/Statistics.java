package com.trafficsimulator.model;

import java.util.ArrayList;
import com.trafficsimulator.utility.HelperMethods;

public class Statistics {

    private int speedMultiplicator = 30; // cell * 20 for example

    private int totalCarsSpawned = 0;
    private double currentAverageSpeed = 0;

    private int currentStationaryCars = 0;
    private int currentAmountOfCars = 0;

    private long totalTicks = 0;
    private double totalAverageSpeed = 0;

    


    public void incrementTotalCarsSpawned() {
        this.totalCarsSpawned++;
    }

    public void update(ArrayList<Car> cars) {
        totalTicks++;

        if (cars.isEmpty()) {
            this.currentAverageSpeed = 0;
            this.currentStationaryCars = 0;
            this.currentAmountOfCars = 0;
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
        this.totalAverageSpeed += this.currentAverageSpeed;
    }

    public int getTotalCarsSpawned() {
        return totalCarsSpawned;
    }

    public double getAverageSpeed() {
        return currentAverageSpeed * speedMultiplicator;
    }

    public int getStationaryCars() {
        return currentStationaryCars;
    }
    public int getCurrentAmountOfCars() {
        return currentAmountOfCars;
    }
    public long getTotalTicks() {
        return totalTicks;
    }
    public double getTotalAverageSpeed() {
        if (totalTicks == 0) {
            return 0;
        }
        return (totalAverageSpeed / totalTicks) * speedMultiplicator;
    }

    public String getFinalReport(double TICK_LENGTH) {
        long totalSeconds = HelperMethods.ticksToTime(totalTicks);
        long h = totalSeconds / 3600;
        long m = (totalSeconds % 3600) / 60;
        long s = totalSeconds % 60;
    
        return String.format(
            "Final Report: \n" +
            "Time Elapsed: %02d:%02d:%02d\n" +
            "Total Ticks: %d\n" +
            "Total Cars Spawned: %d\n" +
            "Final Average Speed: %.2f kmh",
            h, m, s, totalTicks, totalCarsSpawned, getTotalAverageSpeed()
        );
    }
}