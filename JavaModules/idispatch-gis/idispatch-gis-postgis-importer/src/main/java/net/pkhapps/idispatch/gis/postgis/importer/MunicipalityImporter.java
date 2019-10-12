package net.pkhapps.idispatch.gis.postgis.importer;

import net.pkhapps.idispatch.gis.postgis.importer.bindings.CustomFeatureTypeBinding;
import net.pkhapps.idispatch.gis.postgis.importer.bindings.GeographicalNameBinding;
import net.pkhapps.idispatch.gis.postgis.importer.types.LocalizedString;
import org.apache.commons.io.IOUtils;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.gml3.GML;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.PullParser;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.TransformException;
import org.picocontainer.MutablePicoContainer;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command line tool for importing municipalities from NLS XML files into a PostGIS database.
 */
public class MunicipalityImporter {

    private final Connection connection;

    private MunicipalityImporter(@NotNull Connection connection) throws SQLException, IOException {
        System.out.println("Importing data into " + connection);
        var ddl = IOUtils.toString(getClass().getResourceAsStream("/ddl.sql"), StandardCharsets.UTF_8);
        try (var statement = connection.createStatement()) {
            System.out.println("Setting up database");
            statement.execute(ddl);
        }
        this.connection = connection;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments");
        }
        var inputFile = new File(args[0]);
        if (!inputFile.exists() || !inputFile.canRead()) {
            throw new IllegalArgumentException(inputFile + " does not exist or is not readable");
        }
        var jdbcConnectionUrl = args[1];
        var user = args.length <= 2 ? null : args[2];
        var password = args.length <= 3 ? null : args[3];
        try (var connection = DriverManager.getConnection(jdbcConnectionUrl, user, password);
             var inputStream = new FileInputStream(inputFile)) {
            System.out.println("Importing data from " + inputFile.getAbsolutePath());
            new MunicipalityImporter(connection).importData(inputStream, inputFile.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void importData(@NotNull InputStream inputStream, @NotNull String sourceName) throws IOException,
            XMLStreamException, SAXException, TransformException, SQLException {
        var cacheDirectory = new File("iDispatch-GIS-Importer-Cache");
        if (!cacheDirectory.exists() && !cacheDirectory.mkdir()) {
            throw new IOException("Could not create cache directory for resolved schemas");
        }
        var cache = new SchemaCache(cacheDirectory, true);
        var resolver = new SchemaResolver(cache);

        var configuration = new AppSchemaConfiguration("http://xml.nls.fi/inspire/au/4.0/10k",
                "http://xml.nls.fi/inspire/au/4.0/10k/AdministrativeUnit10k.xsd", resolver) {
            @Override
            protected void configureBindings(Map bindings) {
                bindings.put(GeographicalNameBinding.TARGET, new GeographicalNameBinding());
            }

            @Override
            protected void configureContext(MutablePicoContainer container) {
                super.configureContext(container);
                container.registerComponentImplementation(GML.AbstractFeatureType, CustomFeatureTypeBinding.class);
            }
        };
        configuration.addDependency(new WFSConfiguration());

        var parser = new PullParser(configuration, inputStream,
                new QName("http://xml.nls.fi/inspire/au/4.0/10k", "AdministrativeUnit_10k")
        );

        Object feature;
        int count = 0;
        System.out.println("Starting import");

        var sql = "INSERT INTO municipalities (" +
                "national_code," +
                "name_fi," +
                "name_sv," +
                "source," +
                "location," +
                "bounds" +
                ") VALUES (" +
                "?," +
                "?," +
                "?," +
                "?," +
                "ST_GeomFromText(?, 3067)," +
                "ST_GeomFromText(?, 3067)" +
                ")";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            while ((feature = parser.parse()) != null) {
                if (feature instanceof SimpleFeature) {
                    var simpleFeature = (SimpleFeature) feature;
                    if (importFeature(simpleFeature, sourceName, preparedStatement)) {
                        count++;
                    }
                }
            }
        }
        System.out.println("Imported " + count + " municipalities");
    }

    @SuppressWarnings("unchecked")
    private boolean importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                  @NotNull PreparedStatement preparedStatement) throws SQLException {
        var id = (String) feature.getID();
        if (!id.startsWith("FI_AU_ADMINISTRATIVEUNIT_MUNICIPALITY_")) {
            // This is not a municipality so we ignore it.
            return false;
        }
        var name = ((List<LocalizedString>) feature.getAttribute("name")).stream()
                .collect(Collectors.toMap(LocalizedString::getLocale, LocalizedString::getText));
        var location = (Point) feature.getAttribute("location");
        var geometry = (MultiPolygon) feature.getAttribute("geometry");
        var nationalCode = (String) feature.getAttribute("nationalCode");

        var writer = new WKTWriter();

        preparedStatement.setString(1, nationalCode);
        preparedStatement.setString(2, name.get(Locale.forLanguageTag("fi")));
        preparedStatement.setString(3, name.get(Locale.forLanguageTag("sv")));
        preparedStatement.setString(4, sourceName);
        preparedStatement.setString(5, writer.write(location));
        preparedStatement.setString(6, writer.write(geometry));
        preparedStatement.executeUpdate();
        return true;
    }
}
