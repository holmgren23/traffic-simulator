package com.trafficsimulator.model;

/**
 * The available types of obstacles that can be placed on a road.
 */
public enum ObstacleType {
    /** Traffic light that cycles between open and closed on a fixed timer. */
    TRAFFIC_LIGHT,
    /** T-intersection that merges one road into another. */
    INTERSECTION,
    /** Exit point that lets cars leave their current road onto a connected road. */
    EXIT_LANE
}