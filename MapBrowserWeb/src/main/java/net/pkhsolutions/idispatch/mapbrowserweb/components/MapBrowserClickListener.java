/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components;

/**
 * Interface for listening for a {@link MapBrowserClickEvent} fired by a
 * {@link MapBrowserComponent}.
 *
 * @author Petter Holmström
 * @since 1.0
 */
public interface MapBrowserClickListener extends java.io.Serializable {

    /**
     * Called when a {@link MapBrowserComponent} has been clicked.
     *
     * @param event an event containing information about the click.
     */
    void mapBrowserClick(MapBrowserClickEvent event);
}
