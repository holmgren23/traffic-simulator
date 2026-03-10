package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import com.trafficsimulator.model.Road.Direction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RoadView extends Canvas implements SubView {
    protected final Map map;
    private final double windowWidth;
    private final double windowHeight;

    //RoadView constructor that takes the window size to scale the render towards
    public RoadView(TrafficSimulator ts, double windowWidth, double windowHeight) {
        super(windowWidth, windowHeight);
        this.map = ts.getMap();
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
    }

    //X and Y scalars for the map to be rendered
    private double scaleX() {
        return windowWidth / map.getMapWidth();
    }
    private double scaleY() {
        return windowHeight / map.getMapHeight();
    }

    @Override
    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, windowWidth, windowHeight);

        for (Road road : map.getRoads()) {
            drawRoad(gc, road);
            drawRoadObjects(gc, road);
        }
    }

    //Draws the road
    private void drawRoad(GraphicsContext gc, Road road) {
        double x1 = road.getStartX() * scaleX();
        double y1 = road.getStartY() * scaleY();
        double x2 = road.getEndX() * scaleX();
        double y2 = road.getEndY() * scaleY();

        int cells = road.getLength();
        boolean horizontal = road.getDirection() == Direction.EAST || road.getDirection() == Direction.WEST;
        double roadLength = horizontal ? (x2 - x1) : (y2 - y1);
        double cellSize = roadLength / cells;
        double roadThickness = road.getLaneWidth() * scaleY();

        gc.setFill(Color.BLACK);
        if (horizontal) {
            gc.fillRect(x1, y1 - roadThickness / 2, x2 - x1, roadThickness);
        }

        else {
            gc.fillRect(x1 - roadThickness / 2, y1, roadThickness, y2 - y1);
        }

    }

    //Draws cars and obstacles
    private void drawRoadObjects(GraphicsContext gc, Road road) {
        int cells = road.getLength();
        double x1 = road.getStartX() * scaleX();
        double y1 = road.getStartY() * scaleY();
        double x2 = road.getEndX() * scaleX();
        double y2 = road.getEndY() * scaleY();

        boolean horizontal = road.getDirection() == Direction.EAST || road.getDirection() == Direction.WEST;
        boolean reversed = road.getDirection() == Direction.WEST || road.getDirection() == Direction.NORTH;

        double roadLength = horizontal ? (x2 - x1) : (y2 - y1);
        double cellSize = roadLength / cells;

        for (int i = 0; i < cells; i++) {
            int cellIndex = reversed ? cells - 1 - i : i;

            double cellStart = i * cellSize;
            double x = horizontal ? x1 + cellStart : x1;
            double y = horizontal ? y1 : y1 + cellStart;

            //Draw obstacles
            RoadObject obstacle = road.getObstacle(cellIndex);
            if (obstacle instanceof Obstacle obs) {
                drawObstacle(gc, obs, x, y, cellSize, horizontal);
            }

            //Draw vehicles
            RoadObject vehicle = road.getCell(cellIndex);
            if (vehicle instanceof Car car) {
                gc.setFill(car.getColor());
                double carThickness = cellSize * 0.8;
                if (horizontal) {
                    gc.fillRect(x + 1, y - carThickness, cellSize, carThickness);
                } else {
                    gc.fillRect(x - carThickness , y + 1, carThickness, cellSize);
                }
            }
        }
    }

    //Holder for how to draw the different obstacles
    public void drawObstacle(GraphicsContext gc, Obstacle obs, double x, double y, double cellSize, boolean horizontal) {
        double poleWidth = cellSize * 0.4;
        double poleHeight = cellSize * 3;
        double lightSize = cellSize * 1.5;
        
        switch (obs.getType()) {
            case TRAFFIC_LIGHT -> {
                gc.setFill(Color.GRAY);
                if (horizontal) {
                    gc.fillRect(x + cellSize / 2, y - poleHeight, poleWidth, poleHeight);
                } else {
                    gc.fillRect(x - poleHeight, y + cellSize / 2, poleHeight, poleWidth);
                }
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                double poleCenterX = x + cellSize / 2 + poleWidth / 2;
                gc.fillOval(poleCenterX - lightSize / 2, y - poleHeight - lightSize / 2, lightSize, lightSize);
            }
            case INTERSECTION -> {
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                gc.fillOval(x - lightSize / 2, y - lightSize / 2, lightSize, lightSize);
            }
        }
    }
}
