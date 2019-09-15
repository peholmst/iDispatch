module idispatch.core.domain {
    requires java.desktop;
    requires org.geotools.opengis;
    requires org.geotools.referencing;
    requires org.slf4j;

    requires static org.jetbrains.annotations;

    exports net.pkhapps.idispatch.core.domain.common;
    exports net.pkhapps.idispatch.core.domain.geo;
    exports net.pkhapps.idispatch.core.domain.i18n;
    exports net.pkhapps.idispatch.core.domain.incident.model;
}