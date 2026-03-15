module com.trafficsimulator {
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    opens com.trafficsimulator.view to javafx.graphics, javafx.fxml;
    opens com.trafficsimulator.controller to javafx.graphics;
    exports com.trafficsimulator.controller;
    exports com.trafficsimulator.model;
    exports com.trafficsimulator.view;
    exports com.trafficsimulator.startup;
    exports com.trafficsimulator.utility;
    

}
