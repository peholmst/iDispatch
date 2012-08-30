/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * Interface defining a map that can be displayed in a {@link MapBrowser}. The
 * map is divided into a grid consisting of equally wide and equally high cells.
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
     * Returns the coordinates of the point in the center of the map.
     */
    Point.Double getCenterCoordinates();

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

    /**
     * Returns the current zoom level.
     */
    int getZoomLevel();

    /**
     * Sets the current zoom level of the map. If the zoom level falls out of
     * the allowed interval, it is set to the closest ending point (i.e. if the
     * zoom level is less than the minimum zoom level, it is set to the minimum
     * zoom level).
     *
     * @see #getMaximumZoomLevel()
     * @see #getMinimumZoomLevel()
     */
    void setZoomLevel(int zoomLevel);

    /**
     * Returns the number of columns (of equal width) that the map grid has been
     * divided into.
     *
     * @see #getGridColumnWidthInPixels()
     */
    int getGridColumnCount();

    /**
     * Returns the width of a grid column, in pixels.
     *
     * @see #getGridColumnCount()
     */
    int getGridColumnWidthInPixels();

    /**
     * Returns the number of rows (of equal height) that the map grid has been
     * divided into.
     *
     * @see #getGridRowHeightInPixels()
     */
    int getGridRowCount();

    /**
     * Returns the height of a grid row, in pixels.
     *
     * @see #getGridRowCount()
     */
    int getGridRowHeightInPixels();

    /**
     * Returns the lat/long coordinates of the north-west corner of the
     * specified grid cell.
     *
     * @param row the index of the row (starting from 0).
     * @param col the index of the column (starting from 0).
     * @throws IllegalArgumentException if the cell is out of bounds.
     */
    Point.Double getNorthWestCoordinatesOfCell(int row, int col) throws IllegalArgumentException;

    /**
     * Returns the lat/long coordinates of the south-east corner of the
     * specified grid cell.
     *
     * @param row the index of the row (starting from 0).
     * @param col the index of the column (starting from 0).
     * @throws IllegalArgumentException if the cell is out of bounds.
     */
    Point.Double getSouthEastCoordinatesOfCell(int row, int col) throws IllegalArgumentException;

    /**
     * Returns the grid cell that contains the specified location.
     *
     * @param coordinates the coordinates of the location.
     * @throws IllegalArgumentException if the coordinates are out of bounds.
     */
    Point getCellContainingCoordinates(Point.Double coordinates) throws IllegalArgumentException;

    /**
     * TODO Document me (if I'm even needed)
     */
    int convertDistanceBetweenLongitudesToPixels(double longitude1, double longitude2);

    /**
     * TODO Document me (if I'm even needed)
     */
    int convertDistanceBetweenLatitudesToPixels(double latitude1, double latitude2);

    /**
     * Renders the specified grid cell into the specified {@code graphics}
     * object
     *
     * @param graphics the graphics object to render to.
     * @param row the index of the row (starting from 0).
     * @param col the index of the column (starting from 0).
     * @throws IllegalArgumentException if the cell is out of bounds.
     */
    void render(Graphics2D graphics, int row, int col) throws IllegalArgumentException;
}
