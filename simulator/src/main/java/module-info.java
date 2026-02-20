module com.trafficsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.trafficsimulator to javafx.fxml;
    exports com.trafficsimulator;
}
