package com.trafficsimulator.model;

public class StatFlowTracker {
    private static final double MS_PER_MIN = 60000.0;
    
    public double calculateFlow(int count, long ticks, int tickMs) {
        double elapsedMinutes = (ticks * tickMs) / MS_PER_MIN;
        if (elapsedMinutes > 0) {
            return count / elapsedMinutes;
        } else {
            return 0;
        }
    }
}