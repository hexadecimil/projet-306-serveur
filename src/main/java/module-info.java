module app.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires phidget22;
    requires java.sql;
    requires mysql.connector.j;

    opens app.ihm to javafx.fxml;
    exports app;
}