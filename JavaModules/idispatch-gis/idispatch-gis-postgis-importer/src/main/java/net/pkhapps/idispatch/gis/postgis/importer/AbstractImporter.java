package net.pkhapps.idispatch.gis.postgis.importer;

import org.apache.commons.io.IOUtils;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * TODO Document me
 */
public abstract class AbstractImporter {

    private final Connection connection;
    private final SchemaResolver resolver;
    private final AppSchemaConfiguration appSchemaConfiguration;

    protected AbstractImporter(@NotNull Connection connection) throws SQLException, IOException {
        System.out.println("Importing data into " + connection);
        var ddl = IOUtils.toString(getClass().getResourceAsStream("/ddl.sql"), StandardCharsets.UTF_8);
        try (var statement = connection.createStatement()) {
            System.out.println("Setting up database");
            statement.execute(ddl);
        }
        this.connection = connection;

        var cacheDirectory = new File("iDispatch-GIS-Importer-Cache");
        if (!cacheDirectory.exists() && !cacheDirectory.mkdir()) {
            throw new IOException("Could not create cache directory for resolved schemas");
        }
        var cache = new SchemaCache(cacheDirectory, true);
        resolver = new SchemaResolver(cache);

        this.appSchemaConfiguration = createAppSchemaConfiguration();
    }

    protected final @NotNull Connection getConnection() {
        return connection;
    }

    protected final @NotNull SchemaResolver getResolver() {
        return resolver;
    }

    protected abstract @NotNull AppSchemaConfiguration createAppSchemaConfiguration();

    protected @NotNull AppSchemaConfiguration getAppSchemaConfiguration() {
        return appSchemaConfiguration;
    }

    protected abstract void importData(@NotNull InputStream inputStream, @NotNull String sourceName) throws Exception;
}
