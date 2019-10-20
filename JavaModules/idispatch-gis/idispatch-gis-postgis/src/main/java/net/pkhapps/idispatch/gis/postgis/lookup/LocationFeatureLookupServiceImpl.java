package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.lookup.LocationFeature;
import net.pkhapps.idispatch.gis.api.lookup.LocationFeatureLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LocationFeatureLookupService} that looks up the data using JDBC from a PostGIS database.
 */
public class LocationFeatureLookupServiceImpl implements LocationFeatureLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationFeatureLookupServiceImpl.class);
    private final DataSource dataSource;

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
        return null; // TODO Implement me!
    }
}
