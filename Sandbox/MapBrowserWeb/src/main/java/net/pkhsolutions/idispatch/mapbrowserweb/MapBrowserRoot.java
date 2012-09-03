package net.pkhsolutions.idispatch.mapbrowserweb;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.mapbrowserweb.components.MapBrowser;

/**
 * TODO Document me!
 *
 * @author Petter Holmström
 * @since 1.0
 */
public class MapBrowserRoot extends Root {

    @Override
    protected void init(WrappedRequest request) {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        rootLayout.setSizeFull();
        setContent(rootLayout);

        Label title = new Label("Map Browser");
        rootLayout.addComponent(title);

        MapBrowser map = new MapBrowser();
        map.setSizeFull();
        rootLayout.addComponent(map);
        rootLayout.setExpandRatio(map, 1.0f);
    }
}
