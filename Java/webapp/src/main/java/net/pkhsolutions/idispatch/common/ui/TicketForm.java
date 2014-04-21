package net.pkhsolutions.idispatch.common.ui;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.domain.Municipality;
import net.pkhsolutions.idispatch.domain.tickets.TicketModel;
import net.pkhsolutions.idispatch.domain.tickets.TicketType;
import net.pkhsolutions.idispatch.domain.tickets.TicketUrgency;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * Form for viewing/editing a {@link net.pkhsolutions.idispatch.domain.tickets.TicketModel}.
 */
@VaadinComponent
@Scope("prototype")
public class TicketForm extends CustomComponent {

    @PropertyId("description")
    private TextArea description;
    @PropertyId("urgency")
    private ComboBox urgency;
    @PropertyId("ticketType")
    private ComboBox type;
    @PropertyId("municipality")
    private ComboBox municipality;
    @PropertyId("address")
    private TextField address;
    @PropertyId("id")
    private TextField ticketNo;
    @PropertyId("ticketOpened")
    private TextField ticketOpened;
    @PropertyId("ticketClosed")
    private TextField ticketClosed;
    private BeanFieldGroup<TicketModel> binder;

    protected TicketForm() {
    }

    public Optional<TicketModel> getModel() {
        return Optional.ofNullable(binder.getItemDataSource().getBean());
    }

    public void setModel(Optional<TicketModel> model) {
        setReadOnly(false);
        if (model.isPresent()) {
            binder.setItemDataSource(model.get());
            binder.bindMemberFields(this);
        } else {
            throw new UnsupportedOperationException("Not implemented: specifying an empty model");
            // TODO Implement support for specifying an empty model
        }
        setReadOnly(!model.isPresent() || !model.get().isEditable());
    }

    public void setMunicipalities(Collection<Municipality> municipalities) {
        municipality.setContainerDataSource(new BeanItemContainer<>(Municipality.class, municipalities));
        municipality.setItemCaptionPropertyId("name");
    }

    public void setTicketTypes(Collection<TicketType> ticketTypes) {
        type.setContainerDataSource(new BeanItemContainer<>(TicketType.class, ticketTypes));
        type.setItemCaptionPropertyId("formattedDescription");
    }

    @Override
    public boolean isReadOnly() {
        return binder.isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
        binder.setReadOnly(readOnly);
        if (!readOnly) {
            // These fields are always read only
            ticketNo.setReadOnly(true);
            ticketOpened.setReadOnly(true);
            ticketClosed.setReadOnly(true);
        }
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

        binder = new BeanFieldGroup<>(TicketModel.class);
        binder.setBuffered(false);
    }
}
