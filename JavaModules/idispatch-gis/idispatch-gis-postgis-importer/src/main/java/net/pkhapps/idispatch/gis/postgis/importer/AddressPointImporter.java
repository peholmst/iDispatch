package net.pkhapps.idispatch.gis.postgis.importer;

import net.pkhapps.idispatch.gis.postgis.importer.base.FeatureImporterRunner;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Command line tool for importing address points from NLS XML files into a PostGIS database.
 */
public class AddressPointImporter extends AbstractTerrainDataImporter {

    public AddressPointImporter(@NotNull Connection connection) throws SQLException, IOException {
        super(connection, "Osoitepiste");
    }

    public static void main(String[] args) throws Exception {
        FeatureImporterRunner.run(AddressPointImporter.class, args);
    }

    @Override
    protected @NotNull PreparedStatement prepareStatement(@NotNull Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO address_points (" +
                "id," +
                "source," +
                "address_point_class," +
                "number," +
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
                ") VALUES (?,?,?,?,?,ST_GeomFromText(?, 3067),?,?,?,?,?,?,?,?)");
    }

    @Override
    protected boolean importFeature(@NotNull SimpleFeature feature, @NotNull String sourceName,
                                    @NotNull PreparedStatement ps) throws SQLException {
        var id = feature.getID();
        var addressPointClass = toInteger((String) feature.getAttribute("kohdeluokka"));
        var number = (String) feature.getAttribute("numero");
        var municipalityNationalCode = (String) feature.getAttribute("kuntatunnus");
        var nameFi = toString(feature.getAttribute("nimi_suomi"));
        var nameSv = toString(feature.getAttribute("nimi_ruotsi"));
        var nameSmn = toString(feature.getAttribute("nimi_inarinsaame"));
        var nameSms = toString(feature.getAttribute("nimi_koltansaame"));
        var nameSe = toString(feature.getAttribute("nimi_pohjoissaame"));
        var locationAccuracy = toInteger((String) feature.getAttribute("sijaintitarkkuus"));
        var location = (Point) feature.getAttribute("sijainti");
        var validFrom = (Date) feature.getAttribute("alkupvm");
        var validTo = (Date) feature.getAttribute("loppupvm");

        ps.setString(1, id);
        ps.setString(2, sourceName);
        ps.setInt(3, addressPointClass);
        ps.setString(4, number);
        ps.setInt(5, locationAccuracy);
        ps.setString(6, toWkt(location));
        ps.setDate(7, validFrom);
        ps.setDate(8, validTo);
        ps.setString(9, municipalityNationalCode);
        ps.setString(10, nameFi);
        ps.setString(11, nameSv);
        ps.setString(12, nameSe);
        ps.setString(13, nameSmn);
        ps.setString(14, nameSms);
        ps.executeUpdate();
        return true;
    }
}
