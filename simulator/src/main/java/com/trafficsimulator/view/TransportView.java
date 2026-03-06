package com.trafficsimulator.view;

import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;

import com.trafficsimulator.controller.Controller;

public class TransportView extends HBox implements SubView {
  private final String[] BUTTON_LABELS = new String[] {"Pause", "Reset", "Stop"};
  private final ArrayList<EventHandler<ActionEvent>> eventHandlers = new ArrayList<>(
    Arrays.asList(
      e -> {
        this.controller.toggleRunning();
        boolean isRunning = this.controller.isRunning();
        this.buttons[0].setText(isRunning ? " Pause " : "Resume");
      },
      e -> this.controller.resetSimulation(),
      e -> {
        this.controller.stopSimulation();
        MainView.showStatisticsPopup();
        this.mainView.showMenu();
      }
    )
  );
  private Button[] buttons;
  private Controller controller;
  private MainView mainView;

  public TransportView(MainView mainView, Controller controller) {
    this.setMaxWidth(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setMaxHeight(BASELINE_OFFSET_SAME_AS_HEIGHT);
    this.setStyle("-fx-padding: 5;");
    this.setSpacing(4);
    
    this.mainView = mainView;
    this.controller = controller;

    this.buttons = new Button[this.BUTTON_LABELS.length];
    for (int i = 0; i < this.BUTTON_LABELS.length; i++) {
      this.buttons[i] = new Button(this.BUTTON_LABELS[i]);
      this.buttons[i].setOnAction(this.eventHandlers.get(i));
    }
    this.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
      if (e.getCode() != KeyCode.ENTER) { e.consume(); }
    });
    this.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      System.out.println("Key pressed: " + e.getCode());
      switch (e.getCode()) {
        case KeyCode.SPACE:
        case KeyCode.K:
          System.out.println("Pause/Resume");
          this.buttons[0].fire();
          e.consume();
          break;
        case KeyCode.BACK_SPACE:
          System.out.println("Reset");
          this.buttons[1].fire();
          e.consume();
          break;
        case KeyCode.ESCAPE:
          System.out.println("Stop");
          this.buttons[2].fire();
          e.consume();
          break;
      
        default:
          break;
      }
    });
    this.getChildren().addAll(buttons);
  }

  public void update() {
  };
}
