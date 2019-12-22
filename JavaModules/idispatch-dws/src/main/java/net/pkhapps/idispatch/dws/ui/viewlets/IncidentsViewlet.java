package net.pkhapps.idispatch.dws.ui.viewlets;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import net.pkhapps.idispatch.core.client.incident.Incident;
import net.pkhapps.idispatch.core.client.incident.IncidentListingFilter;
import net.pkhapps.idispatch.core.client.incident.IncidentListingService;
import org.jetbrains.annotations.NotNull;

/**
 * TODO Document me!
 */
public class IncidentsViewlet extends AbstractGridViewlet<Incident, IncidentListingFilter> {

    public IncidentsViewlet(@NotNull IncidentListingService incidentListingService) {
        super(incidentListingService);
    }

    @Override
    protected void configureGrid(@NotNull Grid<Incident> grid) {

    }

    @Override
    protected void configureToolbar(@NotNull Div toolbar) {

    }
}
