/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

import com.vaadin.ui.AbstractComponent;
import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
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
    private List<MapBrowserClickListener> listeners = new LinkedList<>();
    private MapInfo mapInfo;
    private final MapBrowserServerRpc rpc = new MapBrowserServerRpc() {
        @Override
        public void componentResized(int widthInPixels, int heightInPixels) {
            MapBrowser.this.componentResized(widthInPixels, heightInPixels);
        }
    };
    private final MapInfo.Listener mapInfoListener = new MapInfo.Listener() {

        @Override
        public void zoomLevelChanged(MapInfo mapInfo, int newZoomLevel) {
            renderMap();
        }
    };
    private int widthInPixels = -1;
    private int heightInPixels = -1;
    
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
        return mapInfo;
    }

    /**
     * Sets the map to display in the browser. If the map is {@code null},
     * nothing is displayed.
     */
    public void setMapInfo(MapInfo mapInfo) {
        if (this.mapInfo != null) {
            this.mapInfo.removeListener(mapInfoListener);
        }
        this.mapInfo = mapInfo;
        if (mapInfo != null) {
            mapInfo.addListener(mapInfoListener);
            // This call will also call renderMap()
            center(mapInfo.getCenterCoordinates());
        } else {
            renderMap();
        }
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
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * Removes the specified listener previously registered using {@link #addListener(MapBrowserClickListener)
     * }. If the listener is {@code null} or has not been previously registered,
     * nothing happens.
     */
    public void removeListener(MapBrowserClickListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void componentResized(int widthInPixels, int heightInPixels) {
        this.widthInPixels = widthInPixels;
        this.heightInPixels = heightInPixels;
    }
    
    private void renderMap() {
        
    }
}
