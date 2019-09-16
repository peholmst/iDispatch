package net.pkhapps.idispatch.core.domain.geo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Value object representing a location that cannot be named.
 */
public class UnnamedLocation extends Location {

    public UnnamedLocation(@NotNull Position coordinates,
                           @Nullable MunicipalityId municipality,
                           @Nullable String additionalDetails) {
        super(coordinates, municipality, additionalDetails);
    }
}
