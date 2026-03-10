package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HighwayView extends RoadView {

    public HighwayView(TrafficSimulator ts, int width) {
        super(ts, width, 160);
    }

    @Override
    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        Road road1 = map.getRoads().get(0);
        Road road2 = map.getRoads().get(1);
        Road road3 = map.getRoads().get(2);
        Road road4 = map.getRoads().get(3);
        int cells = road1.getLength();
        double cellWidth = width / cells;

        drawRoad(gc, width, height);

        // Heldragen mittlinje
        gc.setFill(Color.WHITE);
        gc.fillRect(0, height / 2, width, 2);

        // Streckade linjer för lanes
        drawLines(gc, cellWidth, height * 0.75, cells); // nedre lanes
        drawLines(gc, cellWidth, height * 0.25, cells); // övre lanes
        // Nedre lanes
        drawRoadObjects(gc, road1, width, height, cellWidth, height - 25);
        drawRoadObjects(gc, road2, width, height, cellWidth, height - 70);
        // Övre lanes
        drawRoadObjects(gc, road3, width, height, cellWidth, 15);
        drawRoadObjects(gc, road4, width, height, cellWidth, 50);

    }
}