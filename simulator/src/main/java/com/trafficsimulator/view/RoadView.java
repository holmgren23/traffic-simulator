package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import com.trafficsimulator.model.Road.Direction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public abstract class RoadView extends Canvas implements SubView {
    protected final Map map;

    public RoadView(TrafficSimulator ts, int width, int height) {
        super(width, height);
        this.map = ts.getMap();
    }

    protected void drawRoad(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
    }

    protected void drawLines(GraphicsContext gc, double cellWidth, double y, int cells) {
        gc.setFill(Color.WHITE);
        for(int i = 0; i < cells; i +=3){
            gc.fillRect(i * cellWidth, y, cellWidth, 3);
        }
    }

    private void drawTrafficLight(GraphicsContext gc, Obstacle obs, double x, double yOffset, double cellWidth, Direction direction) {
        double centerX = x + cellWidth / 2;

        switch (direction) {
            case EAST -> {
                gc.setFill(Color.GRAY);
                gc.fillRect(centerX - 2, yOffset - 10, cellWidth, 30);
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                gc.fillOval(centerX - 6, yOffset - 10, 14, 14);
            }
            case WEST -> {
                gc.setFill(Color.GRAY);
                gc.fillRect(centerX - 2, yOffset - 10, cellWidth, 25);
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                gc.fillOval(centerX - 6, yOffset + 10, 14, 14);
            }
            case NORTH -> {
                gc.setFill(Color.GRAY);
                gc.fillRect(x + 10, yOffset - 2, 10, cellWidth);
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                gc.fillOval(x + 20, yOffset - 6, 14, 14);
            }
            case SOUTH -> {
                gc.setFill(Color.GRAY);
                gc.fillRect(x - 10, yOffset - 2, 10, cellWidth);
                gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                gc.fillOval(x - 20, yOffset - 6, 14, 14);
            }
        }
    }

    protected void drawRoadObjects(GraphicsContext gc, Road road, double width, double height, double cellWidth, double yOffset) {
        int cells = road.getLength();
        Direction direction = road.getDirection();
        boolean reversed = direction == Direction.WEST || direction == Direction.NORTH;

        for (int i = 0; i < cells; i++) {
            int cellIndex = reversed ? cells - 1 - i : i;
            double x = i * cellWidth;
            double centerX = x + cellWidth / 2;

            RoadObject obstacle = road.getObstacle(cellIndex);
            if (obstacle instanceof Obstacle obs && obs.getType() == ObstacleType.TRAFFIC_LIGHT) {
                drawTrafficLight(gc, obs, centerX, yOffset, cellWidth, direction);
            }

            // Draw vehicles
            RoadObject vehicle = road.getCell(cellIndex);
            if (vehicle instanceof Car car) {
                gc.setFill(car.getColor());
                gc.fillRect(x + 1, yOffset, cellWidth, height / 8);
            }
        }
    }
    public abstract void update();

}
