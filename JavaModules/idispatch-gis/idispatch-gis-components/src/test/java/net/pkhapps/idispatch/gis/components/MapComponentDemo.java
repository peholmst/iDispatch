package net.pkhapps.idispatch.gis.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("map-component")
public class MapComponentDemo extends VerticalLayout {

    public MapComponentDemo() {
        setSizeFull();
        var mapComponent = new MapComponent();
        mapComponent.setSizeFull();
        add(mapComponent);
    }
}
