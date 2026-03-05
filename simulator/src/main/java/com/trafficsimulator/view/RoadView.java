package com.trafficsimulator.view;

import com.trafficsimulator.model.Obstacle;
import com.trafficsimulator.model.ObstacleType;
import com.trafficsimulator.model.Road;
import com.trafficsimulator.model.RoadObject;
import com.trafficsimulator.model.Car;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RoadView extends Canvas implements SubView {
    private final Road road;

    public RoadView(Road road, int width) {
        super(width, 80);
        this.road = road;
    }

    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        int cells = road.getLength();
        double cellWidth = width / cells;

        // Draw the road
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        // Draw the lines on the road
        gc.setFill(Color.WHITE);
        for (int i = 0; i < cells; i += 3) {
            gc.fillRect(i * cellWidth, getHeight() / 2, cellWidth, getHeight() / 20);
        }
        for (int i = 0; i < cells; i++) {
            // Draw vehicles
            RoadObject obj = road.getCell(i);
            if (obj instanceof Car car) {
                gc.setFill(car.getColor());
                gc.fillRect(width - (i * cellWidth) - cellWidth, 20, cellWidth, height / 8);
                gc.fillRect(i * cellWidth + 1, getHeight() - 20, cellWidth, height / 8);
            }

            // Draw obstacles
            RoadObject obs = road.getObstacle(i);
            if (obs instanceof Obstacle obstacle) {
                if (obstacle.getType() == ObstacleType.TRAFFIC_LIGHT) {
                    double roadMiddle = height / 2;
                    double centerX = i * cellWidth + cellWidth / 2;
                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, 55, cellWidth, roadMiddle - 15);
                    gc.setFill(obstacle.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, 45, 14, 14);
                }
            }
        }
    }
}
