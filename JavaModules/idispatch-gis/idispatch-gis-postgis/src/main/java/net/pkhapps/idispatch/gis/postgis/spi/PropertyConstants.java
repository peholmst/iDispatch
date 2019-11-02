package net.pkhapps.idispatch.gis.postgis.spi;

import java.util.Properties;

/**
 * Configuration properties that are supported by {@link GISFactoryImpl#createGIS(Properties)}.
 */
@SuppressWarnings("SpellCheckingInspection")
public final class PropertyConstants {

    /**
     * The JDBC URL of the PostgreSQL/PostGIS database
     */
    public static final String JDBC_URL = "jdbc.url";
    /**
     * The name of the database user
     */
    public static final String JDBC_USER = "jdbc.user";
    /**
     * The password of the database user
     */
    public static final String JDBC_PASSWORD = "jdbc.password";

    private PropertyConstants() {
    }
}
