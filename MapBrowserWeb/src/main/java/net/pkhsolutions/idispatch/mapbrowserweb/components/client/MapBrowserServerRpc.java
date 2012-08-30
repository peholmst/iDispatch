/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components.client;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

/**
 * TODO Document me!
 *
 * @author Petter Holmström
 * @since 1.0
 */
public interface MapBrowserServerRpc extends ServerRpc {

    /**
     * Called when the MapBrowser component is resized.
     *
     * @param widthInPixels the new width of the component, in pixels.
     * @param heightInPixels the new height of the component, in pixels.
     */
    void componentResized(int widthInPixels, int heightInPixels);
}
