package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.application.lookup.AssignmentTypeLookupService;
import net.pkhapps.idispatch.application.lookup.MunicipalityLookupService;
import net.pkhapps.idispatch.web.ui.common.binding.Binder;
import net.pkhapps.idispatch.web.ui.common.converter.InstantToStringConverter;
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

/**
 * TODO Document me!
 */
@SpringComponent
@ViewScope
class AssignmentDetailsForm extends VerticalLayout {

    private final I18N i18n;
    private final AssignmentTypeLookupService assignmentTypeLookupService;
    private final MunicipalityLookupService municipalityLookupService;
    private final InstantToStringConverter instantToStringConverter;
    private final AssignmentModel model;

    private Binder binder;

    AssignmentDetailsForm(@DispatchQualifier I18N i18n,
                          AssignmentTypeLookupService assignmentTypeLookupService,
                          MunicipalityLookupService municipalityLookupService,
                          InstantToStringConverter instantToStringConverter,
                          AssignmentModel model) {
        this.i18n = i18n;
        this.assignmentTypeLookupService = assignmentTypeLookupService;
        this.municipalityLookupService = municipalityLookupService;
        this.instantToStringConverter = instantToStringConverter;
        this.model = model;
    }

    @PostConstruct
    void init() {
        binder = new Binder();

        TextField id = new TextField(i18n.get("assignmentDetailsForm.id.caption"));
        binder.forField(id).bind(model.idAndVersion());

        TextField opened = new TextField(i18n.get("assignmentDetailsForm.opened.caption"));
        binder.forField(opened).withConverter(instantToStringConverter).bind(model.opened());

        TextField closed = new TextField(i18n.get("assignmentDetailsForm.closed.caption"));
        binder.forField(closed).withConverter(instantToStringConverter).bind(model.closed());

        TextField state = new TextField(i18n.get("assignmentDetailsForm.state.caption"));
        binder.forField(state).withConverter(new AssignmentStateToStringConverter(i18n)).bind(model.state());

        Button close = new Button(i18n.get("assignmentDetailsForm.close.caption"));
        close.addStyleName(DispatchTheme.BUTTON_DANGER);
        binder.forButton(close).bind(model.close());

        TextArea details = new TextArea(i18n.get("assignmentDetailsForm.details.caption"));
        details.setRows(3);
        details.setWidth(100, Unit.PERCENTAGE);
        binder.forField(details).bind(model.description());

        AssignmentTypeComboBox type = new AssignmentTypeComboBox(assignmentTypeLookupService);
        type.setCaption(i18n.get("assignmentDetailsForm.type.caption"));
        binder.forField(type).bind(model.type());

        AssignmentPriorityComboBox priority = new AssignmentPriorityComboBox();
        priority.setCaption(i18n.get("assignmentDetailsForm.priority.caption"));
        binder.forField(priority).bind(model.priority());

        MunicipalityComboBox municipality = new MunicipalityComboBox(municipalityLookupService);
        municipality.setCaption(i18n.get("assignmentDetailsForm.municipality.caption"));
        binder.forField(municipality).bind(model.municipality());

        TextField address = new TextField(i18n.get("assignmentDetailsForm.address.caption"));
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
