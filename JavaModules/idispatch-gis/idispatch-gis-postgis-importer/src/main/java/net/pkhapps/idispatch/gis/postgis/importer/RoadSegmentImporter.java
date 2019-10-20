package net.pkhapps.idispatch.gis.postgis.importer;

import net.pkhapps.idispatch.gis.postgis.importer.base.FeatureImporterRunner;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.sql.*;

/**
 * Command line tool for importing road segments from NLS XML files into a PostGIS database.
 */
public class RoadSegmentImporter extends AbstractTerrainDataImporter {

    public RoadSegmentImporter(@NotNull Connection connection) throws SQLException, IOException {
        super(connection, "Tieviiva");
    }

    public static void main(String[] args) throws Exception {
        FeatureImporterRunner.run(RoadSegmentImporter.class, args);
    }

    @Override
    protected @NotNull PreparedStatement prepareStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO road_segments (" +
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
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,ST_GeomFromText(?, 3067),?,?,?,?,?,?,?,?)");
    }

    @Override
    protected boolean importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                    @NotNull PreparedStatement ps) throws SQLException {
        var id = feature.getID();
        var roadClass = toInteger(feature.getAttribute("kohdeluokka"));
        var elevation = toInteger(feature.getAttribute("tasosijainti"));
        var surface = toInteger(feature.getAttribute("paallyste"));
        var direction = toInteger(feature.getAttribute("yksisuuntaisuus"));
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
        var locationAccuracy = toInteger(feature.getAttribute("sijaintitarkkuus"));
        var location = (LineString) feature.getAttribute("sijainti");
        var validFrom = (Date) feature.getAttribute("alkupvm");
        var validTo = (Date) feature.getAttribute("loppupvm");

        ps.setString(1, id);
        ps.setString(2, sourceName);
        ps.setObject(3, roadClass, Types.INTEGER);
        ps.setObject(4, elevation, Types.INTEGER);
        ps.setObject(5, surface, Types.INTEGER);
        ps.setObject(6, direction, Types.INTEGER);
        ps.setObject(7, roadNumber, Types.BIGINT);
        ps.setObject(8, roadPartNumber, Types.BIGINT);
        ps.setObject(9, addressNumberLeftMin, Types.INTEGER);
        ps.setObject(10, addressNumberLeftMax, Types.INTEGER);
        ps.setObject(11, addressNumberRightMin, Types.INTEGER);
        ps.setObject(12, addressNumberRightMax, Types.INTEGER);
        ps.setObject(13, locationAccuracy, Types.INTEGER);
        ps.setString(14, toWkt(location));
        ps.setDate(15, validFrom);
        ps.setDate(16, validTo);
        ps.setString(17, municipalityNationalCode);
        ps.setString(18, nameFi);
        ps.setString(19, nameSv);
        ps.setString(20, nameSe);
        ps.setString(21, nameSmn);
        ps.setString(22, nameSms);
        ps.executeUpdate();
        return true;
    }
}
