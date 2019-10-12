package net.pkhapps.idispatch.gis.mongodb.importer;

import net.pkhapps.idispatch.gis.mongodb.importer.bindings.CustomFeatureTypeBinding;
import net.pkhapps.idispatch.gis.mongodb.importer.bindings.GeographicalNameBinding;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.gml3.GML;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.geotools.xsd.PullParser;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * TODO Document me!
 */
public class MunicipalityImporter {

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
            new MunicipalityImporter(connection).importData(inputStream, inputFile.getName());
        }
    }

    private MunicipalityImporter(@NotNull Connection connection) throws SQLException {

    }

    @SuppressWarnings("unchecked")
    private void importData(@NotNull InputStream inputStream, @NotNull String sourceName) throws IOException,
            XMLStreamException, SAXException,
            TransformException {
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
        while ((feature = parser.parse()) != null) {
            if (feature instanceof SimpleFeature) {
                var simpleFeature = (SimpleFeature) feature;
                importFeature(simpleFeature, sourceName);
                count++;
            }
        }
        System.out.println("Imported " + count + " municipalities");
    }


    private void importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName) throws TransformException {
        var id = (String) feature.getID();
        if (!id.startsWith("FI_AU_ADMINISTRATIVEUNIT_MUNICIPALITY_")) {
            // This is not a municipality so we ignore it.
            return;
        }
        var name = feature.getAttribute("name");
        var location = (Point) feature.getAttribute("location");
        var geometry = (MultiPolygon) feature.getAttribute("geometry");
        var nationalCode = feature.getAttribute("nationalCode");

        /*var document = new Document();
        document.put("_id", id);
        document.put("nationalCode", nationalCode);
        document.put("name", nameToBson(name));
        document.put("location", toPoint(location));
        document.put("geometry", toMultiPolygon(geometry));
        document.put("source", sourceName);

        collection.insertOne(document);*/
    }
}
