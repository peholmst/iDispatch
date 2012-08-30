package net.pkhsolutions.idispatch.mapbrowserweb;

import com.vaadin.terminal.WrappedRequest;
import com.vaadin.ui.Root;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.mapbrowserweb.components.MapBrowserComponent;

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
        rootLayout.setSizeFull();
        setContent(rootLayout);
        
        MapBrowserComponent map = new MapBrowserComponent();
        map.setSizeFull();
        rootLayout.addComponent(map);
    }
    
}
