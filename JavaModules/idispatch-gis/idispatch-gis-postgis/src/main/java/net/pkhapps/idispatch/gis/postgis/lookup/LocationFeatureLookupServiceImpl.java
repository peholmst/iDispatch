package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.lookup.*;
import net.pkhapps.idispatch.gis.api.lookup.code.*;
import net.pkhapps.idispatch.gis.postgis.bindings.PostgisConverters;
import net.pkhapps.idispatch.gis.postgis.jooq.Routines;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.gis.postgis.jooq.tables.AddressPoints.ADDRESS_POINTS;
import static net.pkhapps.idispatch.gis.postgis.jooq.tables.RoadSegments.ROAD_SEGMENTS;

/**
 * Implementation of {@link LocationFeatureLookupService} that looks up the data using JDBC from a PostGIS database.
 */
public class LocationFeatureLookupServiceImpl implements LocationFeatureLookupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationFeatureLookupServiceImpl.class);
    private static final int QUERY_LIMIT = 20;
    private static final double MAX_DELTA_DISTANCE_METERS = 20;
    private final DataSource dataSource;

    public LocationFeatureLookupServiceImpl(@NotNull DataSource dataSource) {
        this.dataSource = requireNonNull(dataSource);
    }

    @Override
    public @NotNull List<LocationFeature<?>> findFeaturesCloseToPoint(@NotNull Point point) {
        if (point.getSRID() == CRS.WGS84_SRID) {
            point = CRS.wgs84ToEtrs89Tm35Fin(point);
        } else if (point.getSRID() != CRS.ETRS89_TM35FIN_SRID) {
            throw new IllegalArgumentException("Unsupported SRID: " + point.getSRID());
        }
        var result = new ArrayList<LocationFeature<?>>();
        try (var connection = dataSource.getConnection();
             var create = DSL.using(connection)) {
            result.addAll(findAddressPointsCloseToPoint(create, point));
            result.addAll(findRoadSegmentsCloseToPoint(create, point));
        } catch (Exception ex) {
            LOGGER.error("Error looking up features close to point", ex);
        }
        return result;
    }

    @Override
    public @NotNull List<LocationFeature<?>> findFeaturesByName(@Nullable MunicipalityCode municipality,
                                                                @NotNull String name,
                                                                @NotNull NameMatchStrategy matchBy,
                                                                @Nullable String number) {
        var result = new ArrayList<LocationFeature<?>>();
        try (var connection = dataSource.getConnection();
             var create = DSL.using(connection)) {
            result.addAll(findAddressPointsByName(create, municipality, name, matchBy, number));
            result.addAll(findRoadSegmentsByName(create, municipality, name, matchBy, number));
        } catch (Exception ex) {
            LOGGER.error("Error looking up features by name", ex);
        }
        return result;
    }

    private @NotNull List<AddressPoint> findAddressPointsByName(@NotNull DSLContext create,
                                                                @Nullable MunicipalityCode municipality,
                                                                @NotNull String name,
                                                                @NotNull NameMatchStrategy matchBy,
                                                                @Nullable String number) {
        var query = create.select(ADDRESS_POINTS.fields()).from(ADDRESS_POINTS);

        if (municipality != null) {
            query.where(ADDRESS_POINTS.MUNICIPALITY_NATIONAL_CODE.equal(municipality.getCode()));
        }
        if (matchBy == NameMatchStrategy.PREFIX) {
            var searchTerm = name + "%";
            query.where(ADDRESS_POINTS.NAME_FI.likeIgnoreCase(searchTerm)
                    .or(ADDRESS_POINTS.NAME_SV.likeIgnoreCase(searchTerm))
                    .or(ADDRESS_POINTS.NAME_SE.likeIgnoreCase(searchTerm))
                    .or(ADDRESS_POINTS.NAME_SMN.likeIgnoreCase(searchTerm))
                    .or(ADDRESS_POINTS.NAME_SMS.likeIgnoreCase(searchTerm)));
        } else {
            query.where(ADDRESS_POINTS.NAME_FI.equalIgnoreCase(name)
                    .or(ADDRESS_POINTS.NAME_SV.equalIgnoreCase(name))
                    .or(ADDRESS_POINTS.NAME_SE.equalIgnoreCase(name))
                    .or(ADDRESS_POINTS.NAME_SMN.equalIgnoreCase(name))
                    .or(ADDRESS_POINTS.NAME_SMS.equalIgnoreCase(name)));
        }
        if (number != null) {
            query.where(ADDRESS_POINTS.NUMBER.likeIgnoreCase(number + "%"));
        }
        query.orderBy(ADDRESS_POINTS.MUNICIPALITY_NATIONAL_CODE, ADDRESS_POINTS.NAME_FI,
                ADDRESS_POINTS.NAME_SV, ADDRESS_POINTS.NAME_SE, ADDRESS_POINTS.NAME_SMN,
                ADDRESS_POINTS.NAME_SMS, DSL.lpad(ADDRESS_POINTS.NUMBER, 6));
        query.limit(QUERY_LIMIT);
        return query.fetch().stream().map(this::toAddressPoint).collect(Collectors.toList());
    }

    private @NotNull List<AddressPoint> findAddressPointsCloseToPoint(@NotNull DSLContext create,
                                                                      @NotNull Point point) {
        var distance = Routines.stDistance1(ADDRESS_POINTS.LOCATION, DSL.val(PostgisConverters.toPostgis(point), ADDRESS_POINTS.LOCATION));
        var fields = new ArrayList<>(List.of(ADDRESS_POINTS.fields()));
        fields.add(distance);
        // Distance is in meters because of ETRS-TM35FIN
        return create
                .select(fields)
                .from(ADDRESS_POINTS)
                .where(distance.lessOrEqual(MAX_DELTA_DISTANCE_METERS))
                .orderBy(distance)
                .limit(QUERY_LIMIT)
                .fetch()
                .stream()
                .map(this::toAddressPoint)
                .collect(Collectors.toList());
    }

    private @NotNull AddressPoint toAddressPoint(@NotNull Record record) {
        var locationAccuracy = LocationAccuracy.valueOf(record.get(ADDRESS_POINTS.LOCATION_ACCURACY));
        var validFrom = Optional.ofNullable(record.get(ADDRESS_POINTS.VALID_FROM)).map(Date::toLocalDate).orElse(null);
        var validTo = Optional.ofNullable(record.get(ADDRESS_POINTS.VALID_TO)).map(Date::toLocalDate).orElse(null);
        var location = PostgisConverters.fromPostgis((org.postgis.Point) record.get(ADDRESS_POINTS.LOCATION));
        var municipality = MunicipalityCode.of(record.get(ADDRESS_POINTS.MUNICIPALITY_NATIONAL_CODE));
        var nameSv = record.get(ADDRESS_POINTS.NAME_SV);
        var nameFi = record.get(ADDRESS_POINTS.NAME_FI);
        var nameSe = record.get(ADDRESS_POINTS.NAME_SE);
        var nameSmn = record.get(ADDRESS_POINTS.NAME_SMN);
        var nameSms = record.get(ADDRESS_POINTS.NAME_SMS);
        var addressPointClass = AddressPointClass.valueOf(record.get(ADDRESS_POINTS.ADDRESS_POINT_CLASS));
        var number = record.get(ADDRESS_POINTS.NUMBER);
        return new AddressPointImpl(locationAccuracy, validFrom, validTo, location, municipality, nameSv, nameFi,
                nameSe, nameSmn, nameSms, addressPointClass, number);
    }

    private @NotNull Collection<RoadSegment> findRoadSegmentsByName(@NotNull DSLContext create,
                                                                    @Nullable MunicipalityCode municipality,
                                                                    @NotNull String name,
                                                                    @NotNull NameMatchStrategy matchBy,
                                                                    @Nullable String number) {
        var query = create.select(ROAD_SEGMENTS.fields()).from(ROAD_SEGMENTS);
        if (municipality != null) {
            query.where(ROAD_SEGMENTS.MUNICIPALITY_NATIONAL_CODE.equal(municipality.getCode()));
        }
        if (matchBy == NameMatchStrategy.PREFIX) {
            var searchTerm = name + "%";
            query.where(ROAD_SEGMENTS.NAME_FI.likeIgnoreCase(searchTerm)
                    .or(ROAD_SEGMENTS.NAME_SV.likeIgnoreCase(searchTerm))
                    .or(ROAD_SEGMENTS.NAME_SE.likeIgnoreCase(searchTerm))
                    .or(ROAD_SEGMENTS.NAME_SMN.likeIgnoreCase(searchTerm))
                    .or(ROAD_SEGMENTS.NAME_SMS.likeIgnoreCase(searchTerm)));
        } else {
            query.where(ROAD_SEGMENTS.NAME_FI.equalIgnoreCase(name)
                    .or(ROAD_SEGMENTS.NAME_SV.equalIgnoreCase(name))
                    .or(ROAD_SEGMENTS.NAME_SE.equalIgnoreCase(name))
                    .or(ROAD_SEGMENTS.NAME_SMN.equalIgnoreCase(name))
                    .or(ROAD_SEGMENTS.NAME_SMS.equalIgnoreCase(name)));
        }
        extractAddressNumber(number).ifPresent(numericNumber -> query.where(DSL.or(
                ROAD_SEGMENTS.ADDRESS_NUMBER_LEFT_MAX.greaterOrEqual(numericNumber)
                        .and(ROAD_SEGMENTS.ADDRESS_NUMBER_LEFT_MIN.lessOrEqual(numericNumber)),
                ROAD_SEGMENTS.ADDRESS_NUMBER_RIGHT_MAX.greaterOrEqual(numericNumber)
                        .and(ROAD_SEGMENTS.ADDRESS_NUMBER_RIGHT_MIN.lessOrEqual(numericNumber)))));
        query.orderBy(ROAD_SEGMENTS.MUNICIPALITY_NATIONAL_CODE, ROAD_SEGMENTS.NAME_FI,
                ROAD_SEGMENTS.NAME_SV, ROAD_SEGMENTS.NAME_SE, ROAD_SEGMENTS.NAME_SMN,
                ROAD_SEGMENTS.NAME_SMS, ROAD_SEGMENTS.ADDRESS_NUMBER_LEFT_MIN,
                ROAD_SEGMENTS.ADDRESS_NUMBER_RIGHT_MIN);
        query.limit(QUERY_LIMIT);
        return query.fetch().stream().map(this::toRoadSegment).collect(Collectors.toList());
    }

    private @NotNull Collection<RoadSegment> findRoadSegmentsCloseToPoint(@NotNull DSLContext create,
                                                                          @NotNull Point point) {
        var distance = Routines.stDistance1(ROAD_SEGMENTS.LOCATION, DSL.val(PostgisConverters.toPostgis(point), ROAD_SEGMENTS.LOCATION));
        var fields = new ArrayList<>(List.of(ROAD_SEGMENTS.fields()));
        fields.add(distance);
        // Distance is in meters because of ETRS-TM35FIN
        return create
                .select(fields)
                .from(ROAD_SEGMENTS)
                .where(distance.lessOrEqual(MAX_DELTA_DISTANCE_METERS))
                .orderBy(distance)
                .limit(QUERY_LIMIT)
                .fetch()
                .stream()
                .map(this::toRoadSegment)
                .collect(Collectors.toList());
    }

    private @NotNull RoadSegment toRoadSegment(@NotNull Record record) {
        var locationAccuracy = LocationAccuracy.valueOf(record.get(ROAD_SEGMENTS.LOCATION_ACCURACY));
        var validFrom = Optional.ofNullable(record.get(ROAD_SEGMENTS.VALID_FROM)).map(Date::toLocalDate).orElse(null);
        var validTo = Optional.ofNullable(record.get(ROAD_SEGMENTS.VALID_TO)).map(Date::toLocalDate).orElse(null);
        var location = PostgisConverters.fromPostgis((org.postgis.LineString) record.get(ROAD_SEGMENTS.LOCATION));
        var municipality = MunicipalityCode.of(record.get(ROAD_SEGMENTS.MUNICIPALITY_NATIONAL_CODE));
        var nameSv = record.get(ROAD_SEGMENTS.NAME_SV);
        var nameFi = record.get(ROAD_SEGMENTS.NAME_FI);
        var nameSe = record.get(ROAD_SEGMENTS.NAME_SE);
        var nameSmn = record.get(ROAD_SEGMENTS.NAME_SMN);
        var nameSms = record.get(ROAD_SEGMENTS.NAME_SMS);
        var roadClass = RoadClass.valueOf(record.get(ROAD_SEGMENTS.ROAD_CLASS));
        var elevation = Elevation.valueOf(record.get(ROAD_SEGMENTS.ELEVATION));
        var surface = RoadSurface.valueOf(record.get(ROAD_SEGMENTS.SURFACE));
        var direction = RoadDirection.valueOf(record.get(ROAD_SEGMENTS.DIRECTION));
        var roadNumber = record.get(ROAD_SEGMENTS.ROAD_NUMBER);
        var roadPartNumber = record.get(ROAD_SEGMENTS.ROAD_PART_NUMBER);
        var addressNumberLeftMax = record.get(ROAD_SEGMENTS.ADDRESS_NUMBER_LEFT_MAX);
        var addressNumberLeftMin = record.get(ROAD_SEGMENTS.ADDRESS_NUMBER_LEFT_MIN);
        var addressNumberRightMax = record.get(ROAD_SEGMENTS.ADDRESS_NUMBER_RIGHT_MAX);
        var addressNumberRightMin = record.get(ROAD_SEGMENTS.ADDRESS_NUMBER_RIGHT_MIN);
        return new RoadSegmentImpl(locationAccuracy, validFrom, validTo, location, municipality, nameSv, nameFi, nameSe,
                nameSmn, nameSms, roadClass, elevation, surface, direction, roadNumber, roadPartNumber,
                addressNumberLeftMax, addressNumberLeftMin, addressNumberRightMax, addressNumberRightMin);
    }

    @NotNull
    private Optional<Integer> extractAddressNumber(@Nullable String number) {
        if (number == null) {
            return Optional.empty();
        } else {
            var numberPart = new StringBuilder();
            for (int i = 0; i < number.length(); ++i) {
                var ch = number.charAt(i);
                if (Character.isDigit(ch)) {
                    numberPart.append(ch);
                } else {
                    break;
                }
            }
            return numberPart.length() == 0 ? Optional.empty() : Optional.of(Integer.valueOf(numberPart.toString()));
        }
    }
}
