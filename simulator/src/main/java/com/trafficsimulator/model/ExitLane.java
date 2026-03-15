package com.trafficsimulator.model;

public class ExitLane extends Obstacle {
    private Road exitRoad;
    private int exitPosition;

    public ExitLane(int position, Road exitRoad, int exitPosition) {
        super(ObstacleType.EXIT_LANE, position);
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    @Override
    public void calculatePriority(int tickCount) {
        //ExitLane state is determined per car in isOpenFor, not by tick
    }

    @Override
    public boolean isOpenFor(Car car) {
        if (car == null) return true;
        return !car.wantsToTurn();
    }

    public Road getExitRoad() {
        return exitRoad;
    }

    public int getExitPosition() {
        return exitPosition;
    }
}