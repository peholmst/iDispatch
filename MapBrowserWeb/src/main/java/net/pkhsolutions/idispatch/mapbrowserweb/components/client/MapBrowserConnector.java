/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentConnector;
import com.vaadin.terminal.gwt.client.ui.Connect;

/**
 * TODO Document me!
 * 
 * @author Petter Holmström
 * @since 1.0
 */
@Connect(net.pkhsolutions.idispatch.mapbrowserweb.components.MapBrowserComponent.class)
public class MapBrowserConnector extends AbstractComponentConnector {

    @Override
    protected Widget createWidget() {
        return GWT.create(MapBrowserWidget.class);
    }

    @Override
    public MapBrowserWidget getWidget() {
        return (MapBrowserWidget) super.getWidget();
    }
}
