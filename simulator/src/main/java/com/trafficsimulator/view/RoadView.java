package com.trafficsimulator.view;
import com.trafficsimulator.model.Road;
import com.trafficsimulator.model.TrafficSimulator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class RoadView extends Canvas {
    private final Road road;

    public RoadView(TrafficSimulator simulator, int width) {
        super(width, 40);
        this.road = simulator.getRoad();
    }

    public void render(){
        GraphicsContext gc = getGraphicsContext2D();
        double width = getWidth();
        double height = getHeight();
        int cells = road.getLength();
        double cellWidth = width/cells;

        //Draw the road
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
       //Draw the lines on the road
        gc.setFill(Color.WHITE);
        for (int i = 0; i < cells; i+=3){
            gc.fillRect(i * cellWidth, 20, cellWidth, 5);
        }
        //Draw the Cars
        gc.setFill(Color.RED);
        for (int i = 0; i < cells; i++) {
            if (road.getCell(i) != null) {
                gc.fillRect(i * cellWidth + 1, 7, cellWidth, height / 4);
            }
        }
    }
}
