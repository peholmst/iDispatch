package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;

/**
 * Panel used in the {@link AssignmentView} to show a map.
 */
@VaadinComponent
@Scope("prototype")
class AssignmentMapPanel extends VerticalLayout {

    @Autowired
    Environment environment;

    private AssignmentModel assignmentModel;
    private GoogleMap googleMap;

    @PostConstruct
    void init() {
        setSizeFull();
        addStyleName("assignment-map-panel");

        googleMap = new GoogleMap(environment.getProperty("google.map.apiOrClientKey", ""));
        googleMap.setSizeFull();
        addComponent(googleMap);

        // TODO Implement me!
    }

    void setAssignmentModel(AssignmentModel assignmentModel) {
        Assert.notNull(assignmentModel, "TicketModel must not be null");
        this.assignmentModel = assignmentModel;
    }

}
