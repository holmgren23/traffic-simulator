package com.trafficsimulator.model;

public class Intersection extends Obstacle {
    private Road mainRoad;
    private int junctionPosition;
    private int clearanceDistance;
    private Road exitRoad;
    private int exitPosition;

    //Intersection object that closes the road if the junctions point has cars in clearance distance
    public Intersection(int position, Road mainRoad, int junctionPosition, int clearanceDistance) {
        super(ObstacleType.INTERSECTION, position);
        this.mainRoad = mainRoad;
        this.junctionPosition = junctionPosition;
        this.clearanceDistance = clearanceDistance;
    }

    public Intersection(int position, Road mainRoad, int junctionPosition, int clearanceDistance, Road exitRoad, int exitPosition) {
        super(ObstacleType.INTERSECTION, position);
        this.mainRoad = mainRoad;
        this.junctionPosition = junctionPosition;
        this.clearanceDistance = clearanceDistance;
        this.exitRoad = exitRoad;
        this.exitPosition = exitPosition;
    }

    //Priority check for closing and opening the road
    @Override
    public void calculatePriority(int tickCount) {
        setOpen(isJunctionClear());
    }

    //Checks clearance on the connected road
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

    public int getJunctionPosition() {
        return junctionPosition;
    }

    //In Intersection
    public boolean hasExit() { 
        return exitRoad != null; 
    }
    public Road getExitRoad() { 
        return exitRoad; 
    }
    public int getExitPosition() { 
        return exitPosition; 
    }   
}
