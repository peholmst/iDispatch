/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

import com.vaadin.ui.AbstractComponent;
import java.awt.Point;
import net.pkhsolutions.idispatch.mapbrowserweb.components.client.MapBrowserServerRpc;
import net.pkhsolutions.idispatch.mapbrowserweb.components.client.MapBrowserState;

/**
 * Component for browsing a geographical map.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class MapBrowser extends AbstractComponent {

    // TODO Improve documentation once this component is really working.
    
    private final MapBrowserServerRpc rpc = new MapBrowserServerRpc() {
        @Override
        public void componentResized(int widthInPixels, int heightInPixels) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    /**
     * Creates a new {@code MapBrowser}. By default, it has an undefined size.
     */
    public MapBrowser() {
        registerRpc(rpc);
        setSizeUndefined();
    }

    @Override
    public MapBrowserState getState() {
        return (MapBrowserState) super.getState();
    }

    /**
     * Returns the map that is currently being displayed, or {@code null} if no
     * map is being displayed.
     */
    public MapInfo getMapInfo() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Sets the map to display in the browser. If the map is {@code null},
     * nothing is displayed.
     */
    public void setMapInfo(MapInfo mapInfo) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the current zoom level of the map.
     */
    public int getZoomLevel() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Sets the current zoom level of the map. If the zoom level falls out of
     * the allowed interval, it is set to the closest ending point (i.e. if the
     * zoom level is less than the minimum zoom level, it is set to the minimum
     * zoom level).
     *
     * @see MapInfo#getMaximumZoomLevel()
     * @see MapInfo#getMinimumZoomLevel()
     */
    public void setZoomLevel(int zoomLevel) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Centers the map at the specified lat/long coordinates. If the point is
     * out-of-bounds, nothing happens.
     */
    public void center(Point.Double coordinates) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the lat/long coordinates of the north-west corner of the
     * currently visible map.
     */
    public Point.Double getCoordinatesOfNorthWestCorner() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns the lat/long coordinates of the south-east corner of the
     * currently visible map.
     */
    public Point.Double getCoordinatesOfSouthEastCorner() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Adds the specified listener to the set of listeners that are notified
     * when the map is clicked. If the listener is {@code null}, nothing
     * happens.
     */
    public void addListener(MapBrowserClickListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes the specified listener previously registered using {@link #addListener(MapBrowserClickListener)
     * }. If the listener is {@code null} or has not been previously registered,
     * nothing happens.
     */
    public void removeListener(MapBrowserClickListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
