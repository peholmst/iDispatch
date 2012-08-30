/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

/**
 * Interface defining a map that can be displayed in a {@link MapBrowser}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public interface MapInfo extends java.io.Serializable {

    /**
     * Returns the longitude coordinate of the westernmost point on the map.
     */
    double getWesternmostLongitude();

    /**
     * Returns the longitude coordinate of the easternmost point on the map.
     */
    double getEasternmostLongitude();

    /**
     * Returns the latitude coordinate of the northernmost point on the map.
     */
    double getNorthernmostLatitude();

    /**
     * Returns the latitude coordinate of the southernmost point on the map.
     */
    double getSouthernmostLatitude();

    /**
     * Returns the maximum zoom level that the map supports. The larger the zoom
     * level, the more detailed the map.
     */
    int getMaximumZoomLevel();

    /**
     * Returns the minimum zoom level that the map supports. The smaller the
     * zoom level, the less detailed the map.
     */
    int getMinimumZoomLevel();
}
