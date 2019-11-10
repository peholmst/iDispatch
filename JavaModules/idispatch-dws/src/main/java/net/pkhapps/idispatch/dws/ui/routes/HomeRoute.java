package net.pkhapps.idispatch.dws.ui.routes;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import net.pkhapps.idispatch.dws.ui.components.MunicipalityField;

import static net.pkhapps.idispatch.dws.Bootstrapper.services;

@Route("")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")
public class HomeRoute extends VerticalLayout {

    public HomeRoute() {
        var municipality = new MunicipalityField(services().getMunicipalityLookupService())
                .withLabel("Municipality");
        add(municipality);
    }
}
