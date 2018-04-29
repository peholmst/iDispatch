package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupService;
import net.pkhapps.idispatch.application.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.web.ui.common.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.AssignmentTypeComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.MunicipalityLookupComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.UrgencyLookupComboBox;

import javax.annotation.PostConstruct;

@SpringComponent
@ViewScope
class AssignmentDetailsForm extends VerticalLayout {

    private final I18N i18n;
    private final AssignmentTypeLookupService assignmentTypeLookupService;
    private final MunicipalityLookupService municipalityLookupService;

    private TextField id;
    private TextField opened;
    private TextField closed;
    private TextField state;

    private TextArea details;

    private AssignmentTypeComboBox type;
    private UrgencyLookupComboBox urgency;

    private MunicipalityLookupComboBox municipality;
    private TextField address;

    AssignmentDetailsForm(@DispatchQualifier I18N i18n,
                          AssignmentTypeLookupService assignmentTypeLookupService,
                          MunicipalityLookupService municipalityLookupService) {
        this.i18n = i18n;
        this.assignmentTypeLookupService = assignmentTypeLookupService;
        this.municipalityLookupService = municipalityLookupService;
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

        details = new TextArea(i18n.get("assignmentDetailsForm.details.caption"));

        type = new AssignmentTypeComboBox(assignmentTypeLookupService);
        type.setCaption(i18n.get("assignmentDetailsForm.type.caption"));

        urgency = new UrgencyLookupComboBox();
        urgency.setCaption(i18n.get("assignmentDetailsForm.urgency.caption"));

        municipality = new MunicipalityLookupComboBox(municipalityLookupService);
        municipality.setCaption(i18n.get("assignmentDetailsForm.municipality.caption"));

        address = new TextField(i18n.get("assignmentDetailsForm.address.caption"));

        setMargin(false);

        HorizontalLayout headerRow = new HorizontalLayout();
        {
            headerRow.setMargin(false);
            headerRow.addComponentsAndExpand(id, opened, closed, state);
            addComponent(headerRow);
        }

        addComponentsAndExpand(details);

        HorizontalLayout typeRow = new HorizontalLayout();
        {
            typeRow.setMargin(false);
            typeRow.addComponentsAndExpand(type);
            typeRow.addComponent(urgency);
            addComponentsAndExpand(typeRow);
        }

        HorizontalLayout locationRow = new HorizontalLayout();
        {
            locationRow.setMargin(false);
            locationRow.addComponent(municipality);
            locationRow.addComponentsAndExpand(address);
        }
    }

}
