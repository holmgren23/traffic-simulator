package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
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

    protected void drawLines(GraphicsContext gc, double cellWidth, double height, int cells) {
        gc.setFill(Color.WHITE);
        for(int i = 0; i < cells; i +=3){
            gc.fillRect(i * cellWidth, height / 2, cellWidth, height/20);
        }
    }

    protected void drawRoadObjects(GraphicsContext gc, Road road, double width, double height, double cellWidth, double yOffset) {
        int cells = road.getLength();
        boolean reversed = road.isReversed();

        for (int i = 0; i < cells; i++) {
            int cellIndex = reversed ? cells - 1 - i : i;
            double x = i * cellWidth;
            double centerX = x + cellWidth / 2;

            RoadObject obstacle = road.getObstacle(cellIndex);
            if (obstacle instanceof Obstacle obs && obs.getType() == ObstacleType.TRAFFIC_LIGHT) {
                double centerX = x + cellWidth / 2;
                if (reversed) {
                    //top road - draw light below the cars
                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, yOffset - 5, cellWidth, 25);
                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, yOffset + 10, 14, 14);
                } else {
                    //bottom road - draw light above the cars
                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, yOffset - 10, cellWidth, 30);
                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, yOffset - 10, 14, 14);
                }
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
