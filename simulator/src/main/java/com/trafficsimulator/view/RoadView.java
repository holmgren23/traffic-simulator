package com.trafficsimulator.view;

import com.trafficsimulator.model.Obstacle;
import com.trafficsimulator.model.ObstacleType;
import com.trafficsimulator.model.Road;
import com.trafficsimulator.model.RoadObject;
import com.trafficsimulator.model.Car;
import com.trafficsimulator.model.TrafficSimulator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RoadView extends Canvas implements SubView {
    private final Road road;
    private final Road road2;

    public RoadView(TrafficSimulator ts, TrafficSimulator ts2, int width) {
        super(width, 80);
        this.road = ts.getRoad();
        this.road2 = ts2.getRoad();
    }

    public void update(){
        //Road road = this.ts.getRoad();
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
        // Draw the road object
        for (int i = 0; i < cells; i++) {
            RoadObject obstacle = road.getObstacle(i);
            RoadObject vehicle = road.getCell(i);

            if (obstacle instanceof Obstacle obs) {
                if (obs.getType() == ObstacleType.TRAFFIC_LIGHT) {
                    double centerX = i * cellWidth + cellWidth / 2;
                    double roadMiddle = height / 2;

                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, 55, cellWidth, roadMiddle - 15);

                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, 45, 14, 14);
                }
            }

            if (vehicle instanceof Car car) {
                gc.setFill(car.getColor());
                gc.fillRect(i * cellWidth + 1, getHeight() - 20, cellWidth, height / 8);
            }
        }
        //road2
        for (int i = 0; i < cells; i++) {
            RoadObject vehicle2 = road2.getCell(i);
            RoadObject obstacle2 = road2.getObstacle(i);

            if (obstacle2 instanceof Obstacle obs) {
                if (obs.getType() == ObstacleType.TRAFFIC_LIGHT) {
                    double centerX = i * cellWidth + cellWidth / 2 - 1; 
                    double roadMiddle = height / 2;

                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, 15, cellWidth, roadMiddle - 15);

                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, 5, 14, 14);
                }
            }

            if (vehicle2 instanceof Car car) {
                gc.setFill(car.getColor());
                gc.fillRect(width - (i * cellWidth) - cellWidth + 1, 10, cellWidth, height / 8);
            }
        }
    }
}
