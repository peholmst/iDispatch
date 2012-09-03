/*
 * TODO Decide which license to use!
 */
package net.pkhsolutions.idispatch.mapbrowserweb.components.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.communication.RpcProxy;
import com.vaadin.terminal.gwt.client.communication.StateChangeEvent;
import com.vaadin.terminal.gwt.client.ui.AbstractComponentConnector;
import com.vaadin.terminal.gwt.client.ui.Connect;

/**
 * TODO Document me!
 *
 * @author Petter Holmström
 * @since 1.0
 */
@Connect(net.pkhsolutions.idispatch.mapbrowserweb.components.MapBrowser.class)
public class MapBrowserConnector extends AbstractComponentConnector {

    private final MapBrowserServerRpc rpc = RpcProxy.create(MapBrowserServerRpc.class, this);

    @Override
    public MapBrowserState getState() {
        return (MapBrowserState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // TODO Update widget!!!
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(MapBrowserWidget.class);
    }

    @Override
    public MapBrowserWidget getWidget() {
        return (MapBrowserWidget) super.getWidget();
    }
}
