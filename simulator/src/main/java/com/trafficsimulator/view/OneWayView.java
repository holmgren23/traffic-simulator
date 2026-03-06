package com.trafficsimulator.view;

import com.trafficsimulator.model.*;
import javafx.scene.canvas.GraphicsContext;

public class OneWayView extends RoadView {

    public OneWayView(TrafficSimulator ts, int width) {
            super(ts, width, 80);
    }

    @Override
    public void update() {
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        Road road = map.getRoads().get(0);
        int cells = road.getLength();
        double cellWidth = width/cells;

        drawRoad(gc, width, height);
        drawRoadObjects(gc, road, width, height, cellWidth, height/2);
    }
    
}
