package net.pkhapps.idispatch.gis.postgis.lookup;

import net.pkhapps.idispatch.gis.api.CRS;
import net.pkhapps.idispatch.gis.api.lookup.Municipality;
import net.pkhapps.idispatch.gis.api.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.gis.api.lookup.NameMatchStrategy;
import net.pkhapps.idispatch.gis.postgis.bindings.PostgisConverters;
import net.pkhapps.idispatch.gis.postgis.jooq.Routines;
import org.jetbrains.annotations.NotNull;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.idispatch.gis.postgis.jooq.tables.Municipalities.MUNICIPALITIES;

/**
 * Implementation of {@link MunicipalityLookupService} that looks up the data using JDBC from a PostGIS database.
 */
public class MunicipalityLookupServiceImpl implements MunicipalityLookupService {

    private static final Field<?>[] MUNICIPALITY_FIELDS = {MUNICIPALITIES.NATIONAL_CODE, MUNICIPALITIES.NAME_FI,
            MUNICIPALITIES.NAME_SV, MUNICIPALITIES.LOCATION};

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityLookupServiceImpl.class);
    private final DataSource dataSource;

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
             var create = DSL.using(connection)) {
            var result = create
                    .select(MUNICIPALITY_FIELDS)
                    .from(MUNICIPALITIES)
                    .where(Routines.stContains1(MUNICIPALITIES.BOUNDS, DSL.val(PostgisConverters.toPostgis(point), MUNICIPALITIES.BOUNDS)))
                    .fetch();
            return extractSingleResult(result);
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipality of point", ex);
            return Optional.empty();
        }
    }

    @Override
    public @NotNull Optional<Municipality> findByNationalCode(@NotNull String nationalCode) {
        try (var connection = dataSource.getConnection();
             var create = DSL.using(connection)) {
            var result = create
                    .select(MUNICIPALITY_FIELDS)
                    .from(MUNICIPALITIES)
                    .where(MUNICIPALITIES.NATIONAL_CODE.equal(nationalCode))
                    .fetch();
            return extractSingleResult(result);
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipality by national code", ex);
            return Optional.empty();
        }
    }

    @Override
    public @NotNull List<Municipality> findByName(@NotNull String name, @NotNull NameMatchStrategy matchBy) {
        if (name.length() < 2) {
            return Collections.emptyList();
        }
        try (var connection = dataSource.getConnection();
             var create = DSL.using(connection)) {
            var query = create
                    .select(MUNICIPALITY_FIELDS)
                    .from(MUNICIPALITIES);
            if (matchBy == NameMatchStrategy.PREFIX) {
                var searchString = name + "%";
                query.where(MUNICIPALITIES.NAME_FI.likeIgnoreCase(searchString).or(MUNICIPALITIES.NAME_SV.likeIgnoreCase(searchString)));
            } else {
                query.where(MUNICIPALITIES.NAME_FI.equalIgnoreCase(name).or(MUNICIPALITIES.NAME_SV.equalIgnoreCase(name)));
            }
            query.orderBy(MUNICIPALITIES.NAME_FI, MUNICIPALITIES.NAME_SV);
            return extractMultipleResults(query.fetch());
        } catch (Exception ex) {
            LOGGER.error("Error looking up municipalities by name", ex);
            return Collections.emptyList();
        }
    }

    private @NotNull Optional<Municipality> extractSingleResult(@NotNull Result<? extends Record> result) {
        return result.stream().findFirst().map(this::toMunicipality);
    }

    private @NotNull List<Municipality> extractMultipleResults(Result<? extends Record> result) {
        return result.stream().map(this::toMunicipality).collect(Collectors.toList());
    }

    private @NotNull Municipality toMunicipality(@NotNull Record record) {
        var nationalCode = record.get(MUNICIPALITIES.NATIONAL_CODE);
        var nameFi = record.get(MUNICIPALITIES.NAME_FI);
        var nameSv = record.get(MUNICIPALITIES.NAME_SV);
        var center = PostgisConverters.fromPostgis((org.postgis.Point) record.get(MUNICIPALITIES.LOCATION));
        return new MunicipalityImpl(nationalCode, nameFi, nameSv, center);
    }
}
