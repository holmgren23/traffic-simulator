package com.trafficsimulator.model;

public enum ObstacleType {
    TRAFFIC_LIGHT, //Traffic light for single road
    INTERSECTION,   //A T intersection for one-way roads
    EXIT_LANE  //Obstacle that connects as an exit road for cars to leave thier current road
}