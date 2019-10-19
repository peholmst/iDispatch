package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link MunicipalityLookupService} that looks up the data using JDBC from a PostGIS database.
 */
public class MunicipalityLookupServiceImpl implements MunicipalityLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityLookupServiceImpl.class);
    private final DataSource dataSource;
    private final WKTReader wktReader = new WKTReader();
    private final WKTWriter wktWriter = new WKTWriter();

    public MunicipalityLookupServiceImpl(@NotNull DataSource dataSource) {
        this.dataSource = requireNonNull(dataSource);
    }

    @Override
    public @NotNull Optional<Municipality> findMunicipalityOfPoint(@NotNull Point point) {
        if (point.getSRID() == CRS.WGS84_SRID) {
            point = CRS.wgs84ToEtrs89Tm35Fin(point);
        } else if (point.getSRID() != CRS.ETRS89_TM35FIN_SRID) {
            throw new IllegalArgumentException("Unsupported SRID: " + point.getSRID());
        }
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("select national_code, name_fi, name_sv, ST_AsText(location) as location from municipalities where ST_Contains(bounds, ST_GeomFromText(?, ?))")) {
            statement.setString(1, wktWriter.write(point));
            statement.setInt(2, CRS.ETRS89_TM35FIN_SRID);
            statement.execute();
            return extractSingleResult(statement);
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipality of point", ex);
            return Optional.empty();
        }
    }

    @Override
    public @NotNull Optional<Municipality> findByNationalCode(@NotNull String nationalCode) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement("select national_code, name_fi, name_sv, ST_AsText(location) as location from municipalities where national_code = ?")) {
            statement.setString(1, nationalCode);
            statement.execute();
            return extractSingleResult(statement);
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipality by national code", ex);
            return Optional.empty();
        }
    }

    private @NotNull Optional<Municipality> extractSingleResult(@NotNull PreparedStatement statement)
            throws SQLException, ParseException {
        try (var resultSet = statement.getResultSet()) {
            if (resultSet.next()) {
                return Optional.of(toMunicipality(resultSet));
            }
        }
        return Optional.empty();
    }

    @Override
    public @NotNull List<Municipality> findByName(@NotNull String name, @NotNull NameMatchStrategy matchBy) {
        if (name.length() < 2) {
            return Collections.emptyList();
        }
        try (var connection = dataSource.getConnection()) {
            if (matchBy == NameMatchStrategy.PREFIX) {
                return findByNamePrefix(name, connection);
            } else {
                return findByName(name, connection);
            }
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipalities by name", ex);
            return Collections.emptyList();
        }
    }


    private @NotNull List<Municipality> findByNamePrefix(@NotNull String namePrefix, @NotNull Connection connection)
            throws SQLException, ParseException {
        try (var statement = connection.prepareStatement("select national_code, name_fi, name_sv, ST_AsText(location) as location from municipalities where lower(name_fi) like ? or lower(name_sv) like ? order by name_fi, name_sv")) {
            var searchString = namePrefix.toLowerCase() + "%";
            statement.setString(1, searchString);
            statement.setString(2, searchString);
            statement.execute();
            return extractMultipleResults(statement);
        }
    }

    private @NotNull List<Municipality> findByName(@NotNull String name, @NotNull Connection connection)
            throws SQLException, ParseException {
        try (var statement = connection.prepareStatement("select national_code, name_fi, name_sv, ST_AsText(location) as location from municipalities where lower(name_fi) = ? or lower(name_sv) = ? order by name_fi, name_sv")) {
            var searchString = name.toLowerCase();
            statement.setString(1, searchString);
            statement.setString(2, searchString);
            statement.execute();
            return extractMultipleResults(statement);
        }
    }

    private @NotNull List<Municipality> extractMultipleResults(@NotNull PreparedStatement statement)
            throws SQLException, ParseException {
        try (var resultSet = statement.getResultSet()) {
            var list = new ArrayList<Municipality>();
            while (resultSet.next()) {
                list.add(toMunicipality(resultSet));
            }
            return list;
        }
    }

    private @NotNull Municipality toMunicipality(@NotNull ResultSet resultSet) throws SQLException, ParseException {
        var nationalCode = resultSet.getString("national_code");
        var nameFi = resultSet.getString("name_fi");
        var nameSv = resultSet.getString("name_sv");
        var location = resultSet.getString("location");

        var center = (Point) wktReader.read(location);
        center.setSRID(CRS.ETRS89_TM35FIN_SRID);

        return new MunicipalityImpl(nationalCode, nameFi, nameSv, center);
    }
}
