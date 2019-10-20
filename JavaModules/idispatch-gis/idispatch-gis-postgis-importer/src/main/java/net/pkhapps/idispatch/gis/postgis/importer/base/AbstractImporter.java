package net.pkhapps.idispatch.gis.postgis.importer.base;

import org.apache.commons.io.IOUtils;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.PullParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * TODO Document me
 */
public abstract class AbstractImporter {

    private static final WKTWriter WKT_WRITER = new WKTWriter();
    private final Connection connection;
    private final AppSchemaConfiguration appSchemaConfiguration;
    private final QName featureElementName;

    protected AbstractImporter(@NotNull Connection connection, @NotNull QName featureElementName)
            throws SQLException, IOException {
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
        SchemaResolver resolver = new SchemaResolver(cache);

        this.appSchemaConfiguration = createAppSchemaConfiguration(resolver);
        this.featureElementName = featureElementName;
    }

    @Contract("null -> null")
    protected static String toWkt(Geometry geometry) {
        return geometry == null ? null : WKT_WRITER.write(geometry);
    }

    protected abstract @NotNull AppSchemaConfiguration createAppSchemaConfiguration(@NotNull SchemaResolver resolver);

    protected abstract @NotNull PreparedStatement prepareStatement(@NotNull Connection connection) throws SQLException;

    final void importData(@NotNull InputStream inputStream, @NotNull String sourceName) throws Exception {
        var parser = new PullParser(appSchemaConfiguration, inputStream, featureElementName);

        // TODO Add support for importing updates

        Object feature;
        int count = 0;
        System.out.println("Starting import");
        try (var preparedStatement = prepareStatement(connection)) {
            while ((feature = parser.parse()) != null) {
                if (feature instanceof SimpleFeature) {
                    var simpleFeature = (SimpleFeature) feature;
                    if (importFeature(simpleFeature, sourceName, preparedStatement)) {
                        count++;
                    }
                }
            }
        }
        System.out.println("Imported " + count + " features");
    }

    protected abstract boolean importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                             @NotNull PreparedStatement ps) throws SQLException;
}
