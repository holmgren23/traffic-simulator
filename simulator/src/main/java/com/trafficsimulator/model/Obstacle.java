package com.trafficsimulator.model;

public class Obstacle implements RoadObject {
    private int position;
    private ObstacleType type;
    private Boolean open;

    //Creates an obstacle 
    public Obstacle(ObstacleType type, int position) {
        this.type = type;
        this.position = position;
        this.open = false;
    }

    //Getters and setters
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ObstacleType getType() {
        return type;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    //Returns true if a road is accessiable and false if it's not
    public boolean isOpen() {
        return open;
    }

    public boolean isOpenFor(Car car) {
        return isOpen();
    }

    //Calculates which connected roads should be closed or open based on priority rules for that obstacle type
    public void calculatePriority(int tickCount) {
        switch (type) {
            case TRAFFIC_LIGHT -> calculateTrafficLight(tickCount); //Single road traffic light
            case INTERSECTION, EXIT_LANE -> {}
        }
    }

    //Single road traffic light priority rule
    private void calculateTrafficLight(int tickCount) {
        int greenDuration = 20;
        int redDuration = 20;
        //This is maths out to a boolean that is true and false equal to greens/reds duration
        int cycleLength = greenDuration + redDuration;
        open = (tickCount % cycleLength) < greenDuration;
    }
}
