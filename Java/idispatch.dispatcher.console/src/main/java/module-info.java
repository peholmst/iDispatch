module net.pkhapps.idispatch.dispatcher.console {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    requires org.geotools.opengis;
    requires org.geotools.referencing;
    requires org.geotools.epsg_hsql;

    requires org.jetbrains.annotations;

    requires org.locationtech.jts;

    opens net.pkhapps.idispatch.dispatcher.console to javafx.fxml;
    exports net.pkhapps.idispatch.dispatcher.console;
}