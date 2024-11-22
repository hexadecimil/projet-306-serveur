module app.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires phidget22;
    requires java.sql;
    requires mysql.connector.j;
    requires core.video.capture;
    requires core.image;
    requires core.video;
    requires javafx.swing;
//    requires core.image;
//    requires core.video.capture;

    opens app.ihm to javafx.fxml;
    exports app;
}