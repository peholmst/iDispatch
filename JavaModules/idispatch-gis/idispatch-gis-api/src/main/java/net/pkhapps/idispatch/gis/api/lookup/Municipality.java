package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

/**
 * Holds the national code and the names in all applicable languages of a single municipality.
 */
public interface Municipality extends NamedFeature {

    /**
     * Gets the national code of the municipality.
     *
     * @return the national code
     */
    @NotNull
    MunicipalityCode getNationalCode();

    /**
     * Gets the center point of the municipality.
     *
     * @return the center point
     */
    @NotNull
    Point getCenter();
}
