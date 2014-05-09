package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.entity.AssignmentUrgency;
import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.entity.repository.AssignmentTypeRepository;
import net.pkhsolutions.idispatch.entity.repository.MunicipalityRepository;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;

/**
 * Form for viewing/editing an {@link AssignmentModel}.
 */
@VaadinComponent
@Scope("prototype")
class AssignmentForm extends CustomComponent {

    @Autowired
    MunicipalityRepository municipalityRepository;
    @Autowired
    AssignmentTypeRepository assignmentTypeRepository;

    private TextArea description;
    private ComboBox urgency;
    private ComboBox type;
    private ComboBox municipality;
    private TextField address;
    private TextField number;
    private TextField opened;
    private TextField closed;

    void setTicketModel(AssignmentModel assignmentModel) {
        number.setPropertyDataSource(assignmentModel.id());
        opened.setPropertyDataSource(assignmentModel.opened());
        closed.setPropertyDataSource(assignmentModel.closed());
        description.setPropertyDataSource(assignmentModel.description());
        urgency.setPropertyDataSource(assignmentModel.urgency());
        type.setPropertyDataSource(assignmentModel.type());
        municipality.setPropertyDataSource(assignmentModel.municipality());
        address.setPropertyDataSource(assignmentModel.address());
    }

    @PostConstruct
    void init() {
        GridLayout layout = new GridLayout(3, 5);
        layout.setSpacing(true);
        layout.setWidth("100%");
        setCompositionRoot(layout);

        number = new TextField("Number");
        number.setWidth("100%");
        layout.addComponent(number, 0, 0);

        opened = new TextField("Opened");
        opened.setConverter(DateToStringConverter.dateTime());
        opened.setNullRepresentation("");
        opened.setWidth("100%");
        layout.addComponent(opened, 1, 0);

        closed = new TextField("Closed");
        closed.setConverter(DateToStringConverter.dateTime());
        closed.setNullRepresentation("");
        closed.setWidth("100%");
        layout.addComponent(closed, 2, 0);

        description = new TextArea("Description");
        description.setImmediate(true);
        description.setWidth("100%");
        description.setHeight("100px");
        layout.addComponent(description, 0, 1, 2, 1);

        type = new ComboBox("Type");
        type.setImmediate(true);
        type.setWidth("100%");
        layout.addComponent(type, 0, 2, 1, 2);

        urgency = new ComboBox("Urgency", new BeanItemContainer<>(AssignmentUrgency.class, Arrays.asList(AssignmentUrgency.values())));
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

        setMunicipalities(municipalityRepository.findByActiveTrueOrderByNameAsc());
        setTicketTypes(assignmentTypeRepository.findByActiveTrueOrderByCodeAsc());
    }

    private void setMunicipalities(Collection<Municipality> municipalities) {
        municipality.setContainerDataSource(new BeanItemContainer<>(Municipality.class, municipalities));
        municipality.setItemCaptionPropertyId(Municipality.PROP_NAME);
    }

    private void setTicketTypes(Collection<AssignmentType> assignmentTypes) {
        type.setContainerDataSource(new BeanItemContainer<>(AssignmentType.class, assignmentTypes));
        type.setItemCaptionPropertyId(AssignmentType.PROP_FORMATTED_DESCRIPTION);
    }
}
