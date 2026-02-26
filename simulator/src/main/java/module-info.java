module com.trafficsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.trafficsimulator.view to javafx.graphics, javafx.fxml;
    opens com.trafficsimulator.controller to javafx.graphics;
    exports com.trafficsimulator.model;
    exports com.trafficsimulator.view;
    exports com.trafficsimulator.startup;

}
