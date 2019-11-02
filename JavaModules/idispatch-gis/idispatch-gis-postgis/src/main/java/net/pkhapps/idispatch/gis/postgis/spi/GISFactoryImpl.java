package net.pkhapps.idispatch.gis.postgis.spi;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import net.pkhapps.idispatch.gis.api.spi.GIS;
import net.pkhapps.idispatch.gis.api.spi.GISFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Properties;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link GISFactory} that connects to a PostgreSQL/PostGIS database that contains the geographical
 * data. Connection properties are provided in the {@link Properties} instance.
 *
 * @see PropertyConstants
 */
@SuppressWarnings("SpellCheckingInspection")
public class GISFactoryImpl implements GISFactory {

    @Override
    public @NotNull GIS createGIS(@Nullable Properties properties) throws Exception {
        requireNonNull(properties, "this implementation requires a Properties instance");
        var cpds = new ComboPooledDataSource();
        cpds.setDriverClass("org.postgis.DriverWrapper");
        cpds.setJdbcUrl(properties.getProperty(PropertyConstants.JDBC_URL));
        cpds.setUser(properties.getProperty(PropertyConstants.JDBC_USER));
        cpds.setPassword(properties.getProperty(PropertyConstants.JDBC_PASSWORD));
        // TODO Additional configuration of the connection pool
        return new GISImpl(cpds);
    }
}
