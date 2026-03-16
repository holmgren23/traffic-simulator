package com.trafficsimulator.model;

/**
 * The available pre-configured map layouts for the simulation.
 */
public enum MapType {
    /** Two-way road with two lanes per direction. */
    HIGHWAY,
    /** Single one-way road with a traffic light. */
    ONE_WAY,
    /** A road merging into another via a T-shaped intersection. */
    T_INTERSECTION,
    /** Four roads connected in a clockwise loop via intersections. */
    INTERSECTION_LOOP,
    /** Two-way road with five lanes per direction and speed bonuses. */
    AUTOBAHN
}
