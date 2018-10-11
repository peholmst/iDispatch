package net.pkhsolutions.idispatch.ui.reports;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.boundary.ReportDTO;
import net.pkhsolutions.idispatch.boundary.ReportResourceDTO;
import net.pkhsolutions.idispatch.boundary.ReportService;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Optional;

@VaadinView(name = ReportView.VIEW_NAME, ui = ReportsUI.class)
@Scope("prototype")
public class ReportView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "report";

    @Autowired
    ReportService reportService;
    private Label number;
    private Label opened;
    private Label closed;
    private Label description;
    private Label type;
    private Label urgency;
    private Label municipality;
    private Label address;
    private Table resources;
    private BeanItemContainer<ReportResourceDTO> resourcesContainer;

    @PostConstruct
    void init() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();
        GridLayout layout = new GridLayout(3, 5);
        layout.setSpacing(true);
        addComponent(layout);

        number = new Label();
        number.setCaption("Number");
        layout.addComponent(number, 0, 0);

        opened = new Label();
        opened.setCaption("Opened");
        layout.addComponent(opened, 1, 0);

        closed = new Label();
        closed.setCaption("Closed");
        layout.addComponent(closed, 2, 0);

        description = new Label();
        description.setCaption("Description");
        layout.addComponent(description, 0, 1, 2, 1);

        type = new Label();
        type.setCaption("Type");
        layout.addComponent(type, 0, 2, 1, 2);

        urgency = new Label();
        urgency.setCaption("Urgency");
        layout.addComponent(urgency, 2, 2);

        municipality = new Label();
        municipality.setCaption("Municipality");
        layout.addComponent(municipality, 0, 3, 1, 3);

        address = new Label();
        address.setCaption("Address");
        layout.addComponent(address, 0, 4, 2, 4);

        resourcesContainer = new BeanItemContainer<>(ReportResourceDTO.class);
        resources = new Table("Resources");
        resources.setSizeFull();
        resources.setContainerDataSource(resourcesContainer);
        resources.setVisibleColumns(
                ReportResourceDTO.PROP_CALL_SIGN,
                ReportResourceDTO.PROP_DISPATCHED,
                ReportResourceDTO.PROP_EN_ROUTE,
                ReportResourceDTO.PROP_ON_SCENE,
                ReportResourceDTO.PROP_AVAILABLE,
                ReportResourceDTO.PROP_AT_STATION
        );
        resources.setColumnHeaders(
                "Resource",
                "Dispatched",
                "En route",
                "On scene",
                "Available",
                "At station"
        );
        resources.setConverter(ReportResourceDTO.PROP_DISPATCHED, DateToStringConverter.time());
        resources.setConverter(ReportResourceDTO.PROP_EN_ROUTE, DateToStringConverter.time());
        resources.setConverter(ReportResourceDTO.PROP_ON_SCENE, DateToStringConverter.time());
        resources.setConverter(ReportResourceDTO.PROP_AVAILABLE, DateToStringConverter.time());
        resources.setConverter(ReportResourceDTO.PROP_AT_STATION, DateToStringConverter.time());
        addComponent(resources);
        setExpandRatio(resources, 1f);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        final Long assignmentId = getAssignmentId(viewChangeEvent.getParameters());
        final Optional<ReportDTO> report = reportService.findReportByAssignmentId(assignmentId);
        report.ifPresent((r) -> {
            number.setValue(r.getNumber().toString());
            opened.setValue(DateToStringConverter.dateTime().convertToPresentation(r.getOpened(), String.class, getLocale()));
            closed.setValue(DateToStringConverter.dateTime().convertToPresentation(r.getClosed(), String.class, getLocale()));
            description.setValue(r.getDescription());
            type.setValue(r.getType() == null ? "" : r.getType().getFormattedDescription());
            urgency.setValue(r.getUrgency().toString());
            municipality.setValue(r.getMunicipality() == null ? "" : r.getMunicipality().getName());
            address.setValue(r.getAddress());
            resourcesContainer.addAll(r.getResources());
        });

        setEnabled(report.isPresent());
    }

    private long getAssignmentId(String viewParameters) {
        try {
            return Long.valueOf(viewParameters);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
