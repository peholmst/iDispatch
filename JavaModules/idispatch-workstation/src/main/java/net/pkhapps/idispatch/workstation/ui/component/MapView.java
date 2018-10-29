package net.pkhapps.idispatch.workstation.ui.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import net.pkhapps.leaflet4flow.components.LeafletMap;

public class MapView extends Composite<Div> {

    private final Div content;
    private final LeafletMap map;

    public MapView() {
        content = new Div();
        content.setSizeFull();

        map = new LeafletMap();
        map.setSizeFull();

        content.add(map);
    }

    @Override
    protected Div initContent() {
        return content;
    }
}
