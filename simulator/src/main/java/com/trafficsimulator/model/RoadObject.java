package com.trafficsimulator.model;

/**
 * Common interface for all objects that occupy a cell on a road,
 * such as vehicles and obstacles.
 */
public interface RoadObject {
    /**
     * @return Cell position of the object on its road
     */
    int getPosition();

    /**
     * @param position New cell position on the road
     */
    void setPosition(int position);
}
