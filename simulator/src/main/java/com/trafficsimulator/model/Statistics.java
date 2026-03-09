package com.trafficsimulator.model;

import java.util.List;

public class Statistics {
    private static final int TICK_MS = 100;
    private static final int SPEED_MULTIPLIER = 30;

    private int totalCarsSpawned = 0;
    private int totalCarsExited = 0;
    private long totalTicks = 0;
    private double currentAvgSpeed = 0;
    private int currentStationary = 0;
    private int currentCarCount = 0;
    private int previousCarCount = 0;

    public void update(List<Car> cars) {
        totalTicks++;
        this.currentCarCount = cars.size();

        // Update total cars spawned and exited based on the change in car count
        if (currentCarCount > previousCarCount) {     
            totalCarsSpawned += (currentCarCount - previousCarCount);
        } else if (currentCarCount < previousCarCount) {
            totalCarsExited += (previousCarCount - currentCarCount);
        }

        previousCarCount = currentCarCount;
       
        if (cars.isEmpty()) {
            this.currentAvgSpeed = 0;
            this.currentStationary = 0;
            return;
        }
        // Calculate average speed and count stationary cars
        double speedSum = 0;
        int stoppedCount = 0;
        for (Car car : cars) {
            double speed = car.getCurrentSpeed();
            speedSum += speed;
            if (speed == 0) stoppedCount++;
        }
        // Update current stats
        this.currentStationary = stoppedCount;
        this.currentAvgSpeed = speedSum / cars.size();
    }


    public StatSnapshot getSnapshot() {
   
        double totalMinutes = (totalTicks * TICK_MS) / 60000.0;
    
        double avgInFlow = (totalMinutes > 0) ? (totalCarsSpawned / totalMinutes) : 0;

        double avgOutFlow = (totalMinutes > 0) ? (totalCarsExited / totalMinutes) : 0;
    
        return new StatSnapshot(
            totalCarsSpawned,
            currentCarCount,
            currentStationary,
            currentAvgSpeed * SPEED_MULTIPLIER,
            avgInFlow,
            avgOutFlow,
            totalTicks
        );
    }
    // Generates a final report string summarizing the simulation results
    public String getFinalReport() {
        StatSnapshot s = getSnapshot();
        long totalSeconds = (s.totalTicks() * TICK_MS) / 1000;
        
        return String.format(
            "--- FINAL REPORT ---\n" +
            "Time Elapsed: %02d:%02d:%02d\n" +
            "Total Cars Spawned: %d\n" +
            "Total Cars Exited: %d\n" +
            "Average Speed: %.2f km/h\n" +
            "Final InFlow: %.2f veh/m\n" +
            "Final OutFlow: %.2f veh/m",
            totalSeconds / 3600, (totalSeconds % 3600) / 60, totalSeconds % 60,
            s.totalSpawned(), totalCarsExited,
            s.currentAvgSpeedKmh(), s.avgInFlow(), s.avgOutFlow()
        );
    }

 
    public long getTotalTicks() { 
        return totalTicks; 
    }

    public int getTotalCarsSpawned() { 
        return totalCarsSpawned; 
    }
}