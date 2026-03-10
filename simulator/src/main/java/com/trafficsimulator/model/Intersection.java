package com.trafficsimulator.model;

public class Intersection extends Obstacle {
    private Road mainRoad;
    private int junctionPosition;
    private int clearanceDistance;


    public Intersection(int position, Road mainRoad, int junctionPosition, int clearanceDistance) {
        super(ObstacleType.INTERSECTION, position);
        this.mainRoad = mainRoad;
        this.junctionPosition = junctionPosition;
        this.clearanceDistance = clearanceDistance;
    }

    public void calculatePriority(int tickCount) {
        setOpen(isJunctionClear());
    }

    private boolean isJunctionClear() {
        for (int i = junctionPosition - clearanceDistance; i <= junctionPosition + clearanceDistance; i++) {
            if (i >= 0 && i < mainRoad.getLength()) {
                if (mainRoad.getCell(i) != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
