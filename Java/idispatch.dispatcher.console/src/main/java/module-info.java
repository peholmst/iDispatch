module net.pkhapps.idispatch.dispatcher.console {

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires java.ws.rs;
    requires java.xml.bind;

    requires jersey.client;

    requires org.geotools.opengis;
    requires org.geotools.referencing;
    requires org.geotools.epsg_hsql;

    requires org.jetbrains.annotations;

    requires org.kordamp.iconli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    requires org.locationtech.jts;

    requires org.slf4j;
    requires org.slf4j.simple;

    requires spring.security.jwt;

    opens net.pkhapps.idispatch.dispatcher.console to javafx.fxml;
    exports net.pkhapps.idispatch.dispatcher.console;
}