/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

import com.vaadin.ui.Component;
import java.awt.Point;

/**
 * This event is fired when a {@link MapBrowser} is clicked.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class MapBrowserClickEvent extends Component.Event {

    private final Point.Double coordinates;

    /**
     * Creates a new {@code MapBrowserClickEvent}.
     *
     * @param source the map browser that was clicked.
     * @param coordinates the lat/long coordinates of the point where the map
     * was clicked (must not be {@code null}).
     */
    protected MapBrowserClickEvent(MapBrowser source, Point.Double coordinates) {
        super(source);
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates must not be null");
        }
        this.coordinates = coordinates;
    }

    /**
     * Returns the lat/long coordinates of the point where the map was clicked
     * (never {@code null}).
     */
    public Point.Double getCoordinates() {
        return coordinates;
    }

    @Override
    public MapBrowser getComponent() {
        return (MapBrowser) super.getComponent();
    }
}
