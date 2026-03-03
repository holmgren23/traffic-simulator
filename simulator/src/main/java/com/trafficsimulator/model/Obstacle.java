package com.trafficsimulator.model;

public class Obstacle implements RoadObject {
    private int position;
    private ObstacleType type;
    private Boolean open;

    public Obstacle(ObstacleType type, int position) {
        this.type = type;
        this.position = position;
        this.open = false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ObstacleType getType() {
        return type;
    }

    public boolean isOpen() {
        return open;
    }

    public void calculatePriority(int tickCount) {
        switch (type) {
            case TRAFFIC_LIGHT -> calculateTrafficLight(tickCount);
        }
    }

    private void calculateTrafficLight(int tickCount) {
        int greenDuration = 20;
        int redDuration = 20;
        //This is maths out to a boolean that is true and false equal to greens/reds duration
        int cycleLength = greenDuration + redDuration;
        open = (tickCount % cycleLength) < greenDuration;
    }
}
