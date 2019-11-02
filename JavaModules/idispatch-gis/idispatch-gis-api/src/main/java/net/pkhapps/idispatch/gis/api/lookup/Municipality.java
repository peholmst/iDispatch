package net.pkhapps.idispatch.gis.api.lookup;

import net.pkhapps.idispatch.gis.api.lookup.code.MunicipalityCode;
import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Point;

/**
 * Holds the national code and the names in all applicable languages of a single municipality.
 */
public interface Municipality extends NamedFeature {

    /**
     * The national code of the municipality
     */
    @NotNull
    MunicipalityCode getNationalCode();

    /**
     * The center point of the municipality
     */
    @NotNull
    Point getCenter();
}
