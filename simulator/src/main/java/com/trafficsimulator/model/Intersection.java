package com.trafficsimulator.model;

/**
 * Blocks vehicles on a secondary road until the main road is clear.
 * Supports clearance-based and traffic-light-based priority modes.
 */
public class Intersection extends Obstacle {
    private Road mainRoad;
    private int junctionPosition;
    private int clearanceDistance;
    private Road exitRoad;
    private int exitPosition;

    /**
     * Creates an intersection without an exit road.
     * Pass a negative clearance distance to use traffic-light mode instead.
     * @param position Cell position on the secondary road
     * @param mainRoad The main road to check before allowing passage
     * @param junctionPosition Cell position on the main road where the roads meet
     * @param clearanceDistance Cells before junction to check for vehicles. Negative enables traffic-light mode.
     */
    public Intersection(int position, Road mainRoad, int junctionPosition, int clearanceDistance) {
        super(ObstacleType.INTERSECTION, position);
        this.mainRoad = mainRoad;
        this.junctionPosition = junctionPosition;
        this.clearanceDistance = clearanceDistance;
    }

    /**
     * Creates an intersection with an exit road for turning vehicles.
     * @param position Cell position on the secondary road
     * @param mainRoad The main road to check before allowing passage
     * @param junctionPosition Cell position on the main road where the roads meet
     * @param clearanceDistance Cells before junction to check for vehicles. Negative enables traffic-light mode.
     * @param exitRoad Road that turning vehicles are transferred onto
     * @param exitPosition Cell position on the exit road where transferred vehicles are placed
     */
    public Intersection(int position, Road mainRoad, int junctionPosition, int clearanceDistance, Road exitRoad, int exitPosition) {
        super(ObstacleType.INTERSECTION, position);
        this.mainRoad = mainRoad;
        this.junctionPosition = junctionPosition;
        this.clearanceDistance = clearanceDistance;
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    /**
     * Opens or closes the intersection based on whether the junction is clear.
     * @param tickCount Current simulation tick
     */
    @Override
    public void calculatePriority(int tickCount) {
        setOpen(isJunctionClear());
    }

    /**
     * Returns true if no vehicles are within the clearance zone on the main road,
     * or if the main road's traffic light is red (in traffic-light mode).
     * @return True if vehicles may pass through the intersection
     */
    private boolean isJunctionClear() {
        if (clearanceDistance <= -1) {
            return isMainRoadRed();
        }
        for (int i = junctionPosition - clearanceDistance; i < junctionPosition; i++) {
            if (i >= 0 && i < mainRoad.getLength()) {
                if (mainRoad.getCell(i) instanceof Car) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Searches for a traffic light near the junction on the main road.
     * @return True if a red traffic light was found, false otherwise
     */
    private boolean isMainRoadRed() {
        int searchDistance = Math.abs(clearanceDistance);
        for (int i = junctionPosition - searchDistance; i <= junctionPosition + searchDistance; i++) {
            if (i >= 0 && i < mainRoad.getLength()) {
                RoadObject obs = mainRoad.getObstacle(i);
                if (obs instanceof Obstacle o && o.getType() == ObstacleType.TRAFFIC_LIGHT) {
                    return !o.isOpen(); // drive when traffic light is red (main road stopped)
                }
            }
        }
        return false; // no traffic light found, stay closed
    }

    /**
     * @return Cell position on the main road where the roads meet
     */
    public int getJunctionPosition() {
        return junctionPosition;
    }

    /**
     * @return True if this intersection has an exit road configured
     */
    public boolean hasExit() {
        return exitRoad != null;
    }

    /**
     * @return The road turning vehicles are transferred onto, or null if none
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
