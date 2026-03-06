package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HighwayView extends RoadView {
    
    public HighwayView(TrafficSimulator ts, int width) {
        super(ts, width, 80);
    }

    @Override
    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        Road road1 = map.getRoads().get(0);
        Road road2 = map.getRoads().get(1);
        int cells = road1.getLength();
        double cellWidth = width / cells;

        drawRoad(gc, width, height);
        drawLines(gc, cellWidth, height, cells);
        drawRoadObjects(gc, road1, width, height, cellWidth, height - 20);
        drawRoadObjects(gc, road2, width, height, cellWidth, 5);


    }
}