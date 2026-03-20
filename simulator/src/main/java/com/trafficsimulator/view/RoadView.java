package com.trafficsimulator.view;

import java.util.ArrayList;

import com.trafficsimulator.model.*;
import com.trafficsimulator.model.Road.Direction;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * Renders the road network and all road objects (vehicles, obstacles)
 * onto a JavaFX canvas, scaled to fit the window.
 */
public class RoadView extends Canvas implements SubView {
    protected final Map map;
    private final double windowWidth;
    private final double windowHeight;

    /**
     * Creates a RoadView scaled to the given window dimensions.
     * @param ts The simulation model to render
     * @param windowWidth Width of the render canvas in pixels
     * @param windowHeight Height of the render canvas in pixels
     */
    public RoadView(TrafficSimulator ts, double windowWidth, double windowHeight) {
        super(windowWidth, windowHeight);
        this.map = ts.getMap();
        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;
    }

    /**
     * Returns the horizontal scale factor from map units to pixels.
     */
    private double scaleX() {
        return windowWidth / map.getMapWidth();
    }

    /**
     * Returns the vertical scale factor from map units to pixels.
     */
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
        drawDividers(gc);
    }

    /**
     * Draws the black road rectangle for a single road segment,
     * oriented horizontally or vertically based on its direction.
     * @param gc The graphics context to draw on
     * @param road The road segment to draw
     */
    private void drawRoad(GraphicsContext gc, Road road) {
        double x1 = road.getStartX() * scaleX();
        double y1 = road.getStartY() * scaleY();
        double x2 = road.getEndX() * scaleX();
        double y2 = road.getEndY() * scaleY();

        boolean horizontal = road.getDirection() == Direction.EAST || road.getDirection() == Direction.WEST;
        double roadThickness = Math.max(road.getLaneWidth() * scaleY(), 8);

        gc.setFill(Color.BLACK);
        if (horizontal) {
            gc.fillRect(Math.min(x1, x2), y1 - roadThickness / 2, Math.abs(x2 - x1), roadThickness);
        }

        else {
            gc.fillRect(x1 - roadThickness / 2, Math.min(y1, y2), roadThickness, Math.abs(y2 - y1));
        }

    }

    /**
     * Draws lane dividers between adjacent parallel roads.
     * Roads traveling in the same direction get a dashed white line;
     * opposite directions get a solid white line.
     * @param gc The graphics context to draw on
     */
    private void drawDividers(GraphicsContext gc) {
        ArrayList<Road> roads = map.getRoads();
        for (int i = 0; i < roads.size(); i++) {
            for (int j = i + 1; j < roads.size(); j++) {
                Road a = roads.get(i);
                Road b = roads.get(j);

                if (!areParallel(a, b)) continue;
                if (!areAdjacent(a, b)) continue;
                if (a.getDirection() == Direction.NORTH || a.getDirection() == Direction.SOUTH) continue;

                double lineY = ((a.getStartY() + b.getStartY()) / 2.0) * scaleY();
                double x1 = Math.min(a.getStartX(), b.getStartX()) * scaleX();
                double x2 = Math.max(a.getEndX(), b.getEndX()) * scaleX();
                double cellSize = (x2 - x1) / a.getLength();

                boolean sameDirection = a.getDirection() == b.getDirection();

                if (sameDirection) {
                    //dashed white line
                    double dashSize = cellSize * 0.8;
                    gc.setFill(Color.WHITE);
                    for (double x = x1; x < x2; x += cellSize * 3) {
                        gc.fillRect(x, lineY, dashSize, 2);
                    }
                } else {
                    //solid white line
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x1, lineY, x2 - x1, 2);
                }
            }
        }
    }

    /**
     * Returns true if two roads are parallel and share the same start/end coordinates
     * on their shared axis (i.e. they run side by side).
     * @param a First road
     * @param b Second road
     */
    private boolean areParallel(Road a, Road b) {
        boolean bothHorizontal = (a.getDirection() == Direction.EAST || a.getDirection() == Direction.WEST)
                            && (b.getDirection() == Direction.EAST || b.getDirection() == Direction.WEST);
        boolean bothVertical = (a.getDirection() == Direction.NORTH || a.getDirection() == Direction.SOUTH)
                            && (b.getDirection() == Direction.NORTH || b.getDirection() == Direction.SOUTH);

        if (bothHorizontal) {
            return a.getStartX() == b.getStartX() && a.getEndX() == b.getEndX();
        }
        if (bothVertical) {
            return a.getStartY() == b.getStartY() && a.getEndY() == b.getEndY();
        }
        return false;
    }

    /**
     * Returns true if no other road lies between roads {@code a} and {@code b}
     * on the Y axis, meaning they are directly adjacent lanes.
     * @param a First road
     * @param b Second road
     */
    private boolean areAdjacent(Road a, Road b) {
        if (Math.abs(a.getStartY() - b.getStartY()) > 20) return false;
        
        for (Road other : map.getRoads()) {
            if (other == a || other == b) continue;
            if (other.getStartY() > Math.min(a.getStartY(), b.getStartY())
                && other.getStartY() < Math.max(a.getStartY(), b.getStartY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draws all vehicles and obstacles occupying cells on the given road.
     * Each cell is mapped to a pixel position based on the road's direction and scale.
     * @param gc The graphics context to draw on
     * @param road The road whose objects should be drawn
     */
    private void drawRoadObjects(GraphicsContext gc, Road road) {
        int cells = road.getLength();
        double x1 = road.getStartX() * scaleX();
        double y1 = road.getStartY() * scaleY();
        double x2 = road.getEndX() * scaleX();
        double y2 = road.getEndY() * scaleY();

        boolean horizontal = road.getDirection() == Direction.EAST || road.getDirection() == Direction.WEST;
        boolean reversed = road.getDirection() == Direction.WEST || road.getDirection() == Direction.NORTH;

        double roadLength = horizontal ? Math.abs(x2 - x1) : Math.abs(y2 - y1);
        double cellSize = roadLength / cells;

        double drawStartX = horizontal ? Math.min(x1, x2) : x1;
        double drawStartY = horizontal ? y1 : Math.min(y1, y2);

        for (int i = 0; i < cells; i++) {
            int cellIndex = reversed ? cells - 1 - i : i;

            double cellStart = i * cellSize;
            double x = horizontal ? drawStartX + cellStart : drawStartX;
            double y = horizontal ? drawStartY : drawStartY + cellStart;

            //Draw obstacles
            RoadObject obstacle = road.getObstacle(cellIndex);
            if (obstacle instanceof Obstacle obs) {
                drawObstacle(gc, obs, x, y, cellSize, horizontal);
            }

            //Draw vehicles
            RoadObject vehicle = road.getCell(cellIndex);
            if (vehicle instanceof Car car) {
                gc.setFill(car.getColor());
                double carThickness = road.getLaneWidth() * Math.min(scaleX(), scaleY()) * 0.8;
                if (horizontal) {
                    gc.fillRect(x + 1, y - carThickness, cellSize*0.9, carThickness);
                } else {
                    gc.fillRect(x - carThickness, y + 1, carThickness, cellSize);   
                }
            }
        }
    }

    /**
     * Draws a single obstacle at the given canvas position.
     * Rendering varies by obstacle type — traffic lights show a pole and colored signal.
     * @param gc The graphics context to draw on
     * @param obs The obstacle to render
     * @param x Left edge of the cell in pixels
     * @param y Top edge of the cell in pixels
     * @param cellSize Size of one road cell in pixels
     * @param horizontal True if the road runs horizontally, false if vertically
     */
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
            case INTERSECTION, EXIT_LANE -> {}
        }
    }
}
