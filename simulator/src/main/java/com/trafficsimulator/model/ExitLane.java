package com.trafficsimulator.model;

public class ExitLane extends Obstacle {
    private Road exitRoad;
    private int exitPosition;

    //Obstacle that allows cars to exit thier current road onto an other road at a certain road exit position
    public ExitLane(int position, Road exitRoad, int exitPosition) {
        super(ObstacleType.EXIT_LANE, position);
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    @Override
    public void calculatePriority(int tickCount) {
        //ExitLane state is determined per car in isOpenFor, not by tick
    }

    //Override of isOpen. Obstacle opens and closes depening on each indiviual cars intent to turn
    @Override
    public boolean isOpenFor(Car car) {
        return true;
    }

    public boolean tryTurn(Car car, Map map) {
        if (!car.wantsToTurn()) return false;

        if (exitRoad.getCell(exitPosition) != null) {
            return false;  // destination occupied
        }

        car.setCurrentSpeed(0);
        map.transferCar(car, this);
        return true;
    }

    //Getters
    public Road getExitRoad() {
        return exitRoad;
    }

    public int getExitPosition() {
        return exitPosition;
    }
}