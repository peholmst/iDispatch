package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Point;

import java.util.List;

/**
 * TODO Document me
 */
public interface LocationFeatureLookupService {

    @NotNull
    List<LocationFeature<?>> findFeaturesCloseToPoint(@NotNull Point point);

    @NotNull
    List<LocationFeature<?>> findFeaturesByName(@Nullable MunicipalityCode municipality, @NotNull String name,
                                                @NotNull NameMatchStrategy matchBy, @Nullable String number);
}
