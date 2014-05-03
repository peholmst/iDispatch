package net.pkhsolutions.idispatch.dws.ui.tickets;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.common.ui.DateToStringConverter;
import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.MunicipalityRepository;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import net.pkhsolutions.idispatch.domain.tickets.TicketTypeRepository;
import net.pkhsolutions.idispatch.domain.tickets.TicketUrgency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;

/**
 * Form for viewing/editing a {@link TicketModel}.
 */
@VaadinComponent
@Scope("prototype")
class TicketForm extends CustomComponent {

    @Autowired
    MunicipalityRepository municipalityRepository;
    @Autowired
    TicketTypeRepository ticketTypeRepository;

    private TextArea description;
    private ComboBox urgency;
    private ComboBox type;
    private ComboBox municipality;
    private TextField address;
    private TextField ticketNo;
    private TextField ticketOpened;
    private TextField ticketClosed;

    protected TicketForm() {
    }

    void setTicketModel(TicketModel ticketModel) {
        Assert.notNull(ticketModel, "TicketModel must not be null");
        ticketNo.setPropertyDataSource(ticketModel.id());
        ticketOpened.setPropertyDataSource(ticketModel.ticketOpened());
        ticketClosed.setPropertyDataSource(ticketModel.ticketClosed());
        description.setPropertyDataSource(ticketModel.description());
        urgency.setPropertyDataSource(ticketModel.urgency());
        type.setPropertyDataSource(ticketModel.ticketType());
        municipality.setPropertyDataSource(ticketModel.municipality());
        address.setPropertyDataSource(ticketModel.address());
    }

    @PostConstruct
    void init() {
        GridLayout layout = new GridLayout(3, 5);
        layout.setSpacing(true);
        layout.setWidth("100%");
        setCompositionRoot(layout);

        ticketNo = new TextField("Number");
        ticketNo.setWidth("100%");
        layout.addComponent(ticketNo, 0, 0);

        ticketOpened = new TextField("Opened");
        ticketOpened.setConverter(DateToStringConverter.dateTime());
        ticketOpened.setNullRepresentation("");
        ticketOpened.setWidth("100%");
        layout.addComponent(ticketOpened, 1, 0);

        ticketClosed = new TextField("Closed");
        ticketClosed.setConverter(DateToStringConverter.dateTime());
        ticketClosed.setNullRepresentation("");
        ticketClosed.setWidth("100%");
        layout.addComponent(ticketClosed, 2, 0);

        description = new TextArea("Ticket details");
        description.setImmediate(true);
        description.setWidth("100%");
        description.setHeight("100px");
        layout.addComponent(description, 0, 1, 2, 1);

        type = new ComboBox("Incident type");
        type.setImmediate(true);
        type.setWidth("100%");
        layout.addComponent(type, 0, 2, 1, 2);

        urgency = new ComboBox("Urgency", new BeanItemContainer<>(TicketUrgency.class, Arrays.asList(TicketUrgency.values())));
        urgency.setImmediate(true);
        urgency.setWidth("100%");
        layout.addComponent(urgency, 2, 2);

        municipality = new ComboBox("Municipality");
        municipality.setImmediate(true);
        municipality.setWidth("100%");
        layout.addComponent(municipality, 0, 3, 1, 3);

        address = new TextField("Address");
        address.setImmediate(true);
        address.setWidth("100%");
        layout.addComponent(address, 0, 4, 2, 4);

        setMunicipalities(municipalityRepository.findAll());
        setTicketTypes(ticketTypeRepository.findAll());
    }

    private void setMunicipalities(Collection<Municipality> municipalities) {
        municipality.setContainerDataSource(new BeanItemContainer<>(Municipality.class, municipalities));
        municipality.setItemCaptionPropertyId("name");
    }

    private void setTicketTypes(Collection<TicketType> ticketTypes) {
        type.setContainerDataSource(new BeanItemContainer<>(TicketType.class, ticketTypes));
        type.setItemCaptionPropertyId("formattedDescription");
    }
}
