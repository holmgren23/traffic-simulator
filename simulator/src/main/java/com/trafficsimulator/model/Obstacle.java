package com.trafficsimulator.model;

/**
 * A stationary road object that can block or allow vehicles
 * depending on its type and open/closed state.
 */
public class Obstacle implements RoadObject {
    private int position;
    private ObstacleType type;
    private Boolean open;

    /**
     * Creates an obstacle of the given type at the given position.
     * Starts in a closed (blocking) state.
     * @param type The type of obstacle (e.g. traffic light, intersection)
     * @param position Cell position of the obstacle on its road
     */
    public Obstacle(ObstacleType type, int position) {
        this.type = type;
        this.position = position;
        this.open = false;
    }

    /**
     * @return Cell position of the obstacle on its road
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position New cell position on the road
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return The type of this obstacle
     */
    public ObstacleType getType() {
        return type;
    }

    /**
     * @param open True to allow vehicles through, false to block them
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * @return True if vehicles can pass through this obstacle, false if blocked
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Returns whether this obstacle is open for a specific car.
     * Subclasses may override this to apply car-specific rules.
     * @param car The car attempting to pass
     * @return True if the car can pass through this obstacle
     */
    public boolean isOpenFor(Car car) {
        return isOpen();
    }

    /**
     * Updates the open/closed state of this obstacle based on its type
     * and priority rules for the current tick.
     * @param tickCount Current simulation tick
     */
    public void calculatePriority(int tickCount) {
        switch (type) {
            case TRAFFIC_LIGHT -> calculateTrafficLight(tickCount); //Single road traffic light
            case INTERSECTION, EXIT_LANE -> {} //
        }
    }

    /**
     * Calculates the open/closed state for a single-road traffic light
     * using a fixed green and red duration cycle.
     * @param tickCount Current simulation tick
     */
    private void calculateTrafficLight(int tickCount) {
        int greenDuration = 20;
        int redDuration = 20;
        //This is maths out to a boolean that is true and false equal to greens/reds duration
        int cycleLength = greenDuration + redDuration;
        open = (tickCount % cycleLength) < greenDuration;
    }
}
