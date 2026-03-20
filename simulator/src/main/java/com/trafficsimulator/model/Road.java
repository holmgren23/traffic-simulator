package com.trafficsimulator.model;

/**
 * Represents a single road segment in the simulation.
 * A road holds two parallel layers of cells: one for vehicles and one for obstacles,
 * allowing both to occupy the same position independently.
 */
public class Road {
    private RoadObject[] vehicles;
    private RoadObject[] obstacles;
    private Direction direction;
    private int startX, startY;
    private int endX, endY;
    private Road exitRoad;
    private int exitPosition;
    private int laneWidth;
    private Road overtakeLane;  // faster lane to move to when blocked
    private Road mergeLane;     // slower lane to return to when clear
    private boolean isSpawnRoad = true;
    private int speedBonus = 0;

    /** Cardinal directions a road can travel. */
    public enum Direction {
        EAST, WEST, NORTH, SOUTH
    }

    /**
     * Creates a road segment with separate vehicle and obstacle cell layers.
     * @param length Number of cells on this road
     * @param direction The direction vehicles travel on this road
     * @param startX Start X coordinate in map units
     * @param startY Start Y coordinate in map units
     * @param endX End X coordinate in map units
     * @param endY End Y coordinate in map units
     * @param laneWidth Visual width of the lane in map units
     */
    public Road(int length, Direction direction, int startX, int startY, int endX, int endY, int laneWidth) {
        vehicles = new RoadObject[length];
        obstacles = new RoadObject[length];
        this.direction = direction;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.laneWidth = laneWidth;
    }

    /**
     * Returns the number of cells on this road.
     */
    public int getLength() {
        return vehicles.length;
    }

    /**
     * Returns the direction vehicles travel on this road.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the visual width of the lane in map units.
     */
    public int getLaneWidth() {
        return laneWidth;
    }

    /**
     * Sets the road that vehicles transfer to when they reach the end of this road,
     * and the cell index they appear at on the exit road.
     * @param exitRoad The road to transfer vehicles to
     * @param exitPosition The cell index on the exit road where vehicles appear
     */
    public void setExit(Road exitRoad, int exitPosition) {
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    /**
     * Returns the road vehicles are transferred to at the end of this road.
     */
    public Road getExitRoad() {
        return exitRoad;
    }

    /**
     * Returns the cell index on the exit road where vehicles appear after transfer.
     */
    public int getExitPosition() {
        return exitPosition;
    }

    /**
     * Returns true if this road has a connected exit road.
     * Vehicles with no exit are sent to the removal queue.
     */
    public boolean hasExit() {
        return exitRoad != null;
    }

    /** Returns the start X coordinate in map units. */
    public int getStartX() {return startX;}
    /** Returns the start Y coordinate in map units. */
    public int getStartY() {return startY;}
    /** Returns the end X coordinate in map units. */
    public int getEndX() {return endX;}
    /** Returns the end Y coordinate in map units. */
    public int getEndY() {return endY;}

    /**
     * Returns the vehicle (or null) at the given cell index.
     * @param index Cell index on this road
     */
    public RoadObject getCell(int index) {
        return vehicles[index];
    }

    /**
     * Sets the vehicle at the given cell index.
     * @param index Cell index on this road
     * @param obj The road object to place, or null to clear the cell
     */
    public void setCell(int index, RoadObject obj) {
        vehicles[index] = obj;
    }

    /**
     * Returns the obstacle (or null) at the given cell index.
     * @param index Cell index on this road
     */
    public RoadObject getObstacle(int index) {
        return obstacles[index];
    }

    /**
     * Sets the obstacle at the given cell index.
     * @param index Cell index on this road
     * @param obj The obstacle to place, or null to clear the cell
     */
    public void setObstacle(int index, RoadObject obj) {
        obstacles[index] = obj;
    }

    /**
     * Sets the faster adjacent lane vehicles can move to when blocked.
     * @param lane The overtake lane road
     */
    public void setOvertakeLane(Road lane) {
        this.overtakeLane = lane;
    }

    /**
     * Returns the overtake lane, or null if none is configured.
     */
    public Road getOvertakeLane() {
        return overtakeLane;
    }

    /**
     * Sets the slower adjacent lane vehicles return to after overtaking.
     * @param lane The merge lane road
     */
    public void setMergeLane(Road lane) {
        this.mergeLane = lane;
    }

    /**
     * Returns the merge lane, or null if none is configured.
     */
    public Road getMergeLane() {
        return mergeLane;
    }

    /**
     * Returns true if this road is eligible to spawn new vehicles.
     */
    public boolean isSpawnRoad() {
        return isSpawnRoad;
    }

    /**
     * Sets whether this road should spawn new vehicles.
     * @param isSpawnRoad True to allow spawning, false to disable
     */
    public void setSpawnRoad(boolean isSpawnRoad) {
        this.isSpawnRoad = isSpawnRoad;
    }

    /**
     * Returns the speed bonus applied to vehicles on this road.
     */
    public int getSpeedBonus() {
        return speedBonus;
    }

    /**
     * Sets the speed bonus for vehicles on this road.
     * @param speedBonus Extra speed added on top of a vehicle's base speed
     */
    public void setSpeedBonus(int speedBonus) {
        this.speedBonus = speedBonus;
    }
}
