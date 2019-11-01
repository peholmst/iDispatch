package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.AddressPoint;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeature;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LocationFeatureLookupService} that looks up the data using JDBC from a PostGIS database.
 */
public class LocationFeatureLookupServiceImpl implements LocationFeatureLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationFeatureLookupServiceImpl.class);
    private final DataSource dataSource;
    private final WKTReader wktReader = new WKTReader();
    private final WKTWriter wktWriter = new WKTWriter();

    public LocationFeatureLookupServiceImpl(@NotNull DataSource dataSource) {
        this.dataSource = requireNonNull(dataSource);
    }

    @Override
    public @NotNull Collection<LocationFeature<?>> findFeaturesCloseToPoint(@NotNull Point point) {
        return null; // TODO Implement me!
    }

    @Override
    public @NotNull Collection<LocationFeature<?>> findFeaturesByName(@Nullable MunicipalityCode municipality,
                                                                      @NotNull String name,
                                                                      @NotNull NameMatchStrategy matchBy,
                                                                      @Nullable String number) {
        var sql = new StringBuilder();

        try (var connection = dataSource.getConnection()) {
            findAddressPointsByName(connection, municipality, name, matchBy, number);
        } catch (Exception ex) {
            LOGGER.error("Error looking up features by name", ex);
            return Collections.emptyList();
        }
        return null; // TODO Implement me!
    }

    private @NotNull Collection<AddressPoint> findAddressPointsByName(@NotNull Connection connection,
                                                                      @Nullable MunicipalityCode municipality,
                                                                      @NotNull String name,
                                                                      @NotNull NameMatchStrategy matchBy,
                                                                      @Nullable String number) throws SQLException {
        var sql = "select address_point_class, number, " +
                "location_accuracy, valid_from, valid_to, ST_AsText(location) as location, municipality_national_code, " +
                "name_fi, name_sv, name_se, name_smn, name_sms " +
                "from address_points";
        if (municipality != null) {
            //sql = sql + " municipality_national_code = :municipality"
        }
        try (var statement = connection.prepareStatement(sql)) {

            return null;
        }
    }

}
