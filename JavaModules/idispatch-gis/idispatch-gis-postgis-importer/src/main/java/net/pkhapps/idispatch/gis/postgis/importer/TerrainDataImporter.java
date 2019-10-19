package net.pkhapps.idispatch.gis.postgis.importer;

import org.geotools.appschema.resolver.xml.AppSchemaConfiguration;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.PullParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.WKTWriter;
import org.opengis.feature.simple.SimpleFeature;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.Map;

/**
 * Command line tool for importing terrain data from NLS XML files into a PostGIS database.
 */
public class TerrainDataImporter extends AbstractImporter {

    private TerrainDataImporter(@NotNull Connection connection) throws SQLException, IOException {
        super(connection);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Insufficient arguments");
        }
        var inputDirectory = new File(args[0]);
        if (!inputDirectory.exists() || !inputDirectory.isDirectory()) {
            throw new IllegalArgumentException(inputDirectory + " does not exist or is not a directory");
        }
        var jdbcConnectionUrl = args[1];
        var user = args.length <= 2 ? null : args[2];
        var password = args.length <= 3 ? null : args[3];
        try (var connection = DriverManager.getConnection(jdbcConnectionUrl, user, password)) {
            var importer = new TerrainDataImporter(connection);
            try (var fileStream = Files.newDirectoryStream(inputDirectory.toPath(), "*.xml")) {
                for (var file : fileStream) {
                    try (var inputStream = new FileInputStream(file.toFile())) {
                        System.out.println("Importing data from " + file.toAbsolutePath());
                        importer.importData(inputStream, file.getFileName().toString());
                    }
                }
            }
        }
    }

    // TODO Add support for importing updates

    private static void setLong(@NotNull PreparedStatement preparedStatement, int parameterIndex,
                                @Nullable Long value) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        } else {
            preparedStatement.setLong(parameterIndex, value);
        }
    }

    private static void setInt(@NotNull PreparedStatement preparedStatement, int parameterIndex,
                               @Nullable Integer value) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(parameterIndex, Types.INTEGER);
        } else {
            preparedStatement.setInt(parameterIndex, value);
        }
    }

    @Contract("null -> null")
    private static Integer toInteger(String s) {
        return s == null ? null : Integer.valueOf(s);
    }

    @Contract("null -> null")
    private static String toString(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Map) {
            return toString(((Map) o).get(null));
        } else {
            return o.toString();
        }
    }

    @Override
    protected @NotNull AppSchemaConfiguration createAppSchemaConfiguration() {
        var configuration = new AppSchemaConfiguration(
                "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02",
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd", getResolver());
        configuration.addDependency(new GMLConfiguration());
        return configuration;
    }

    @Override
    protected void importData(@NotNull InputStream inputStream, @NotNull String sourceName) throws Exception {
        var parser = new PullParser(getAppSchemaConfiguration(), inputStream,
                new QName("http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02", "Tieviiva")
        );

        Object feature;
        int count = 0;
        System.out.println("Starting import");

        var sql = "INSERT INTO road_segments (" +
                "id," +
                "source," +
                "road_class," +
                "elevation," +
                "surface," +
                "direction," +
                "road_number," +
                "road_part_number," +
                "address_number_left_min," +
                "address_number_left_max," +
                "address_number_right_min," +
                "address_number_right_max," +
                "location_accuracy," +
                "location," +
                "valid_from," +
                "valid_to," +
                "municipality_national_code," +
                "name_fi," +
                "name_sv," +
                "name_se," +
                "name_smn," +
                "name_sms" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,ST_GeomFromText(?, 3067),?,?,?,?,?,?,?,?)";
        try (var preparedStatement = getConnection().prepareStatement(sql)) {
            while ((feature = parser.parse()) != null) {
                if (feature instanceof SimpleFeature) {
                    var simpleFeature = (SimpleFeature) feature;
                    if (importRoadSegment(simpleFeature, sourceName, preparedStatement)) {
                        count++;
                    }
                }
            }
        }
        System.out.println("Imported " + count + " road segments");
    }

    private boolean importRoadSegment(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                      @NotNull PreparedStatement preparedStatement) throws SQLException {
        var id = feature.getID();
        var roadClass = toInteger((String) feature.getAttribute("kohdeluokka"));
        var elevation = toInteger((String) feature.getAttribute("tasosijainti"));
        var surface = toInteger((String) feature.getAttribute("paallyste"));
        var direction = toInteger((String) feature.getAttribute("yksisuuntaisuus"));
        var roadNumber = (Long) feature.getAttribute("tienumero");
        var roadPartNumber = (Long) feature.getAttribute("tieosanumero");
        var addressNumberLeftMin = (Integer) feature.getAttribute("minOsoitenumeroVasen");
        var addressNumberLeftMax = (Integer) feature.getAttribute("maxOsoitenumeroVasen");
        var addressNumberRightMin = (Integer) feature.getAttribute("minOsoitenumeroOikea");
        var addressNumberRightMax = (Integer) feature.getAttribute("maxOsoitenumeroOikea");
        var municipalityNationalCode = (String) feature.getAttribute("kuntatunnus");
        var nameFi = toString(feature.getAttribute("nimi_suomi"));
        var nameSv = toString(feature.getAttribute("nimi_ruotsi"));
        var nameSmn = toString(feature.getAttribute("nimi_inarinsaame"));
        var nameSms = toString(feature.getAttribute("nimi_koltansaame"));
        var nameSe = toString(feature.getAttribute("nimi_pohjoissaame"));
        var locationAccuracy = toInteger((String) feature.getAttribute("sijaintitarkkuus"));
        var location = (LineString) feature.getAttribute("sijainti");
        var validFrom = (Date) feature.getAttribute("alkupvm");
        var validTo = (Date) feature.getAttribute("loppupvm");

        var writer = new WKTWriter();

        preparedStatement.setString(1, id);
        preparedStatement.setString(2, sourceName);
        preparedStatement.setInt(3, roadClass);
        preparedStatement.setInt(4, elevation);
        preparedStatement.setInt(5, surface);
        preparedStatement.setInt(6, direction);
        setLong(preparedStatement, 7, roadNumber);
        setLong(preparedStatement, 8, roadPartNumber);
        setInt(preparedStatement, 9, addressNumberLeftMin);
        setInt(preparedStatement, 10, addressNumberLeftMax);
        setInt(preparedStatement, 11, addressNumberRightMin);
        setInt(preparedStatement, 12, addressNumberRightMax);
        preparedStatement.setInt(13, locationAccuracy);
        preparedStatement.setString(14, writer.write(location));
        preparedStatement.setDate(15, validFrom);
        preparedStatement.setDate(16, validTo);
        preparedStatement.setString(17, municipalityNationalCode);
        preparedStatement.setString(18, nameFi);
        preparedStatement.setString(19, nameSv);
        preparedStatement.setString(20, nameSe);
        preparedStatement.setString(21, nameSmn);
        preparedStatement.setString(22, nameSms);
        preparedStatement.executeUpdate();
        return true;
    }
}
