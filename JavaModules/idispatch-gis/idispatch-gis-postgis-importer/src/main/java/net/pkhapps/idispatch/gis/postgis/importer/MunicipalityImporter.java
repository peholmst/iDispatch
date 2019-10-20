package net.pkhapps.idispatch.gis.postgis.importer;

import net.pkhapps.idispatch.gis.postgis.importer.base.AbstractImporter;
import net.pkhapps.idispatch.gis.postgis.importer.base.FeatureImporterRunner;
import net.pkhapps.idispatch.gis.postgis.importer.bindings.CustomFeatureTypeBinding;
import net.pkhapps.idispatch.gis.postgis.importer.bindings.GeographicalNameBinding;
import net.pkhapps.idispatch.gis.postgis.importer.types.LocalizedString;
import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.gml3.GML;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.xml.resolver.SchemaResolver;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.picocontainer.MutablePicoContainer;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command line tool for importing municipalities from NLS XML files into a PostGIS database.
 */
public class MunicipalityImporter extends AbstractImporter {

    public MunicipalityImporter(@NotNull Connection connection) throws SQLException, IOException {
        super(connection, new QName("http://xml.nls.fi/inspire/au/4.0/10k", "AdministrativeUnit_10k"));
    }

    public static void main(String[] args) throws Exception {
        FeatureImporterRunner.run(MunicipalityImporter.class, args);
    }

    @Override
    protected @NotNull AppSchemaConfiguration createAppSchemaConfiguration(@NotNull SchemaResolver resolver) {
        var configuration = new AppSchemaConfiguration("http://xml.nls.fi/inspire/au/4.0/10k",
                "http://xml.nls.fi/inspire/au/4.0/10k/AdministrativeUnit10k.xsd", resolver) {

            @SuppressWarnings("unchecked")
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
        return configuration;
    }

    @Override
    protected @NotNull PreparedStatement prepareStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO municipalities (" +
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
                ")");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                    @NotNull PreparedStatement ps) throws SQLException {
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

        ps.setString(1, nationalCode);
        ps.setString(2, name.get(Locale.forLanguageTag("fi")));
        ps.setString(3, name.get(Locale.forLanguageTag("sv")));
        ps.setString(4, sourceName);
        ps.setString(5, toWkt(location));
        ps.setString(6, toWkt(geometry));
        ps.executeUpdate();
        return true;
    }
}
