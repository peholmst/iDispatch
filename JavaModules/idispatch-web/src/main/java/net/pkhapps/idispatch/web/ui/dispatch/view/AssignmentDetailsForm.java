package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.application.assignment.AssignmentDetailsDTO;
import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupService;
import net.pkhapps.idispatch.application.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.web.ui.common.I18N;
import net.pkhapps.idispatch.web.ui.common.StringToInstantConverter;
import net.pkhapps.idispatch.web.ui.dispatch.DispatchTheme;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.AssignmentPriorityComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.AssignmentTypeComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.MunicipalityComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentModel;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;

@SpringComponent
@ViewScope
class AssignmentDetailsForm extends VerticalLayout implements AssignmentModel.Observer {

    private final I18N i18n;
    private final AssignmentTypeLookupService assignmentTypeLookupService;
    private final MunicipalityLookupService municipalityLookupService;
    private final StringToInstantConverter stringToInstantConverter;

    private TextField id;
    private TextField opened;
    private TextField closed;
    private TextField state;

    private Button close;

    private TextArea details;

    private AssignmentTypeComboBox type;
    private AssignmentPriorityComboBox urgency;

    private MunicipalityComboBox municipality;
    private TextField address;

    private Binder<AssignmentDetailsDTO> binder;

    AssignmentDetailsForm(@DispatchQualifier I18N i18n,
                          AssignmentTypeLookupService assignmentTypeLookupService,
                          MunicipalityLookupService municipalityLookupService,
                          StringToInstantConverter stringToInstantConverter) {
        this.i18n = i18n;
        this.assignmentTypeLookupService = assignmentTypeLookupService;
        this.municipalityLookupService = municipalityLookupService;
        this.stringToInstantConverter = stringToInstantConverter;
    }

    @PostConstruct
    void init() {
        id = new TextField(i18n.get("assignmentDetailsForm.id.caption"));
        id.setReadOnly(true);

        opened = new TextField(i18n.get("assignmentDetailsForm.opened.caption"));
        opened.setReadOnly(true);

        closed = new TextField(i18n.get("assignmentDetailsForm.closed.caption"));
        closed.setReadOnly(true);

        state = new TextField(i18n.get("assignmentDetailsForm.state.caption"));
        state.setReadOnly(true);

        close = new Button(i18n.get("assignmentDetailsForm.close.caption"));
        close.addStyleName(DispatchTheme.BUTTON_DANGER);

        details = new TextArea(i18n.get("assignmentDetailsForm.details.caption"));
        details.setRows(3);
        details.setWidth(100, Unit.PERCENTAGE);

        type = new AssignmentTypeComboBox(assignmentTypeLookupService);
        type.setCaption(i18n.get("assignmentDetailsForm.type.caption"));

        urgency = new AssignmentPriorityComboBox();
        urgency.setCaption(i18n.get("assignmentDetailsForm.urgency.caption"));

        municipality = new MunicipalityComboBox(municipalityLookupService);
        municipality.setCaption(i18n.get("assignmentDetailsForm.municipality.caption"));

        address = new TextField(i18n.get("assignmentDetailsForm.address.caption"));

        setMargin(false);

        HorizontalLayout headerRow = new HorizontalLayout();
        {
            headerRow.setMargin(false);
            headerRow.addComponentsAndExpand(id, opened, closed, state);
            headerRow.addComponent(close);
            headerRow.setComponentAlignment(close, Alignment.BOTTOM_LEFT);
            addComponent(headerRow);
        }

        addComponent(details);

        HorizontalLayout typeRow = new HorizontalLayout();
        {
            typeRow.setMargin(false);
            typeRow.addComponentsAndExpand(type);
            typeRow.addComponent(urgency);
            addComponent(typeRow);
        }

        HorizontalLayout locationRow = new HorizontalLayout();
        {
            locationRow.setMargin(false);
            locationRow.addComponent(municipality);
            locationRow.addComponentsAndExpand(address);
            addComponent(locationRow);
        }

        binder = new Binder<>(AssignmentDetailsDTO.class);
        binder.forField(id).bind(AssignmentDetailsDTO::getIdAndVersion, null);
        binder.forField(opened).withConverter(stringToInstantConverter).bind(AssignmentDetailsDTO::getOpened, null);
        binder.forField(closed).withConverter(stringToInstantConverter).bind(AssignmentDetailsDTO::getClosed, null);
    }

    @Nullable
    AssignmentDetailsDTO getDTO() {
        return binder.getBean();
    }

    void setDTO(@Nullable AssignmentDetailsDTO dto) {
        binder.setBean(dto);
    }
}
