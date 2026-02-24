module com.trafficsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.trafficsimulator.view to javafx.graphics, javafx.fxml;

    exports com.trafficsimulator.view;
    exports com.trafficsimulator.startup;

}
