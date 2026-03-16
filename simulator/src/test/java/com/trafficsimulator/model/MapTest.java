package com.trafficsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trafficsimulator.model.Road.Direction;

class MapTest {
    private Map map;
    private Road road;

    @BeforeEach
    void setUp() {
        map = new Map(MapType.ONE_WAY);
        road = new Road(200, Direction.EAST, 0, 0, 100, 100, 5);
        map.addRoad(road);
    }

    @Test
    void testGap_obstacleClosedBlocksCar() {
        Obstacle obs = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 5);
        map.addObstacle(obs, road);
        assertEquals(4, map.gap(road, 0));
    }

    @Test
    void testGap_obstacleOpenIsPassable() {
        Obstacle obs = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 5);
        map.addObstacle(obs, road);
        // run enough ticks to get green
        for (int i = 0; i < 10; i++) {
            map.updateObstacles(i);
        }
        assertEquals(Integer.MAX_VALUE, map.gap(road, 0));
    }

    @Test
    void testAddObstacle_placedOnCorrectLayer() {
        Obstacle obs = new Obstacle(ObstacleType.TRAFFIC_LIGHT, 10);
        map.addObstacle(obs, road);
        assertNotNull(road.getObstacle(10));
        assertNull(road.getCell(10)); // should not be on vehicle layer
    }

    @Test
    void testGetObjectAhead_returnsCorrectObject() {
        Car car = new Car(0, 5, 5, 0.2);
        road.setCell(5, car);
        assertEquals(car, map.getObjectAhead(road, 0));
    }

    @Test
    void testGetObjectAhead_noObjectAhead_returnsNull() {
        assertEquals(null, map.getObjectAhead(road, 0));
    }
}
