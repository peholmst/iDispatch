package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupService;
import net.pkhapps.idispatch.application.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.web.ui.common.binding.Binder;
import net.pkhapps.idispatch.web.ui.common.converter.StringToInstantConverter;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.DispatchTheme;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import net.pkhapps.idispatch.web.ui.dispatch.converter.AssignmentStateToStringConverter;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.AssignmentPriorityComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.AssignmentTypeComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.lookup.MunicipalityComboBox;
import net.pkhapps.idispatch.web.ui.dispatch.model.AssignmentModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpringComponent
@ViewScope
class AssignmentDetailsForm extends VerticalLayout {

    private final I18N i18n;
    private final AssignmentTypeLookupService assignmentTypeLookupService;
    private final MunicipalityLookupService municipalityLookupService;
    private final StringToInstantConverter stringToInstantConverter;
    private final AssignmentModel model;

    private TextField id;
    private TextField opened;
    private TextField closed;
    private TextField state;

    private Button close;

    private TextArea details;

    private AssignmentTypeComboBox type;
    private AssignmentPriorityComboBox priority;

    private MunicipalityComboBox municipality;
    private TextField address;

    private Binder binder;

    AssignmentDetailsForm(@DispatchQualifier I18N i18n,
                          AssignmentTypeLookupService assignmentTypeLookupService,
                          MunicipalityLookupService municipalityLookupService,
                          StringToInstantConverter stringToInstantConverter,
                          AssignmentModel model) {
        this.i18n = i18n;
        this.assignmentTypeLookupService = assignmentTypeLookupService;
        this.municipalityLookupService = municipalityLookupService;
        this.stringToInstantConverter = stringToInstantConverter;
        this.model = model;
    }

    @PostConstruct
    void init() {
        binder = new Binder();

        id = new TextField(i18n.get("assignmentDetailsForm.id.caption"));
        id.setReadOnly(true);
        binder.forField(id).bind(model.idAndVersion());

        opened = new TextField(i18n.get("assignmentDetailsForm.opened.caption"));
        binder.forField(opened).withConverter(stringToInstantConverter).bind(model.opened());

        closed = new TextField(i18n.get("assignmentDetailsForm.closed.caption"));
        binder.forField(closed).withConverter(stringToInstantConverter).bind(model.closed());

        state = new TextField(i18n.get("assignmentDetailsForm.state.caption"));
        binder.forField(state).withConverter(new AssignmentStateToStringConverter(i18n)).bind(model.state());

        close = new Button(i18n.get("assignmentDetailsForm.close.caption"));
        close.addStyleName(DispatchTheme.BUTTON_DANGER);
        binder.forButton(close).bind(model.close());

        details = new TextArea(i18n.get("assignmentDetailsForm.details.caption"));
        details.setRows(3);
        details.setWidth(100, Unit.PERCENTAGE);
        binder.forField(details).bind(model.description());

        type = new AssignmentTypeComboBox(assignmentTypeLookupService);
        type.setCaption(i18n.get("assignmentDetailsForm.type.caption"));
        binder.forField(type).bind(model.type());

        priority = new AssignmentPriorityComboBox();
        priority.setCaption(i18n.get("assignmentDetailsForm.priority.caption"));
        binder.forField(priority).bind(model.priority());

        municipality = new MunicipalityComboBox(municipalityLookupService);
        municipality.setCaption(i18n.get("assignmentDetailsForm.municipality.caption"));
        binder.forField(municipality).bind(model.municipality());

        address = new TextField(i18n.get("assignmentDetailsForm.address.caption"));
        binder.forField(address).bind(model.address());

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
            typeRow.addComponent(priority);
            addComponent(typeRow);
        }

        HorizontalLayout locationRow = new HorizontalLayout();
        {
            locationRow.setMargin(false);
            locationRow.addComponent(municipality);
            locationRow.addComponentsAndExpand(address);
            addComponent(locationRow);
        }
    }

    @PreDestroy
    void destroy() {
        binder.removeAllBindings();
    }
}
