package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RoadView extends Canvas implements SubView {
    private final Map map;

    public RoadView(TrafficSimulator ts, int width) {
        super(width, 80);
        this.map = ts.getMap();
    }

    public void update(){
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        List<Road> roads = map.getRoads();
        int cells = roads.get(0).getLength();
        double cellWidth = width / cells;

        //Draw the road
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        //Draw the lines on the road
        gc.setFill(Color.WHITE);
        for (int i = 0; i < cells; i += 3) {
            gc.fillRect(i * cellWidth, getHeight() / 2, cellWidth, getHeight() / 20);
        }

        for (int r = 0; r < roads.size(); r++) {
            boolean reversed = r % 2 != 0;
            double yOffset = reversed ? 5 : height - 20;
            drawRoad(gc, roads.get(r), width, height, cellWidth, yOffset, reversed);
        }        
    }

    //Draw the road object
    private void drawRoad(GraphicsContext gc, Road road, double width, double height, double cellWidth, double yOffset, boolean reversed) {
        int cells = road.getLength();

        for (int i = 0; i < cells; i++) {
            int cellIndex = reversed ? cells - 1 - i : i;
            double x = i * cellWidth;

            //Draw obstacles
            RoadObject obstacle = road.getObstacle(cellIndex);
            if (obstacle instanceof Obstacle obs && obs.getType() == ObstacleType.TRAFFIC_LIGHT) {
                double centerX = x + cellWidth / 2;
                if (reversed) {
                    //top road - draw light below the cars
                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, yOffset + 10, cellWidth, 10);
                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, yOffset + 20, 14, 14);
                } else {
                    //bottom road - draw light above the cars
                    gc.setFill(Color.GRAY);
                    gc.fillRect(centerX - 2, yOffset - 10, cellWidth, 10);
                    gc.setFill(obs.isOpen() ? Color.GREEN : Color.RED);
                    gc.fillOval(centerX - 6, yOffset - 20, 14, 14);
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
}
