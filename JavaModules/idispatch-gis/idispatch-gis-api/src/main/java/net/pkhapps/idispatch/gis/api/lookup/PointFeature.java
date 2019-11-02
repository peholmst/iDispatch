package net.pkhapps.idispatch.gis.api.lookup;

import org.locationtech.jts.geom.Point;

/**
 * A {@linkplain LocationFeature location feature} whose location information is in the form of a {@link Point}.
 */
@SuppressWarnings("WeakerAccess")
public interface PointFeature extends LocationFeature<Point> {
}
