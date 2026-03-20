package com.trafficsimulator.model;

/**
 * A mid-road exit point that allows vehicles to transfer onto another road.
 * The exit is always structurally open, but passage is controlled per vehicle
 * based on whether the vehicle intends to turn.
 */
public class ExitLane extends Obstacle {
    private Road exitRoad;
    private int exitPosition;

    /**
     * Creates an exit lane at the given position on the current road.
     * @param position Cell position of the exit lane on the current road
     * @param exitRoad The road the vehicle will transfer onto when turning
     * @param exitPosition Cell position on the exit road where the vehicle will be placed
     */
    public ExitLane(int position, Road exitRoad, int exitPosition) {
        super(ObstacleType.EXIT_LANE, position);
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    /**
     * No tick-based priority logic for exit lanes.
     * Open/closed state is determined per vehicle in {@link #isOpenFor(Car)}.
     * @param tickCount Current simulation tick
     */
    @Override
    public void calculatePriority(int tickCount) {
        //ExitLane state is determined per car in isOpenFor, not by tick
    }

    /**
     * Returns whether this exit lane is passable for a specific vehicle.
     * Exit lanes are always structurally open; turn intent is checked separately in {@link #tryTurn(Car, Map)}.
     * @param car The car attempting to pass
     * @return Always true
     */
    @Override
    public boolean isOpenFor(Car car) {
        return true;
    }

    /**
     * Attempts to transfer the vehicle onto the exit road.
     * The transfer is cancelled if the vehicle does not intend to turn
     * or if the target cell on the exit road is occupied.
     * @param car The car attempting to turn
     * @param map The current map, used to perform the transfer
     * @return True if the vehicle was successfully transferred, false otherwise
     */
    public boolean tryTurn(Car car, Map map) {
        if (!car.wantsToTurn()) return false;

        if (exitRoad.getCell(exitPosition) != null) {
            return false;  // destination occupied
        }

        car.setCurrentSpeed(0);
        map.transferCar(car, this);
        return true;
    }

    /**
     * @return The road vehicles transfer onto when using this exit lane
     */
    public Road getExitRoad() {
        return exitRoad;
    }

    /**
     * @return Cell position on the exit road where transferred vehicles are placed
     */
    public int getExitPosition() {
        return exitPosition;
    }
}