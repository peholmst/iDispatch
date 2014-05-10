package net.pkhsolutions.idispatch.ui.dws.assignments;

import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
import net.pkhsolutions.idispatch.boundary.events.AssignmentEvent;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.AssignmentTypeToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.MunicipalityToStringConverter;
import net.pkhsolutions.idispatch.ui.dws.DwsTheme;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListenerMethod;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

@VaadinView(name = AssignmentTableView.VIEW_NAME, ui = DwsUI.class)
@UIScope
class AssignmentTableView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "assignmentTable";

    @Autowired
    AssignmentService assignmentService;
    @Autowired
    EventBus eventBus;
    @Autowired
    AssignmentTypeToStringConverter assignmentTypeToStringConverter;
    @Autowired
    MunicipalityToStringConverter municipalityToStringConverter;

    private Table table;
    private BeanItemContainer<Assignment> container;
    private Button openAssignment;

    @PostConstruct
    void init() {
        // TODO Internationalize
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final Label title = new Label("Open Assignments");
        title.addStyleName(DwsTheme.LABEL_H1);
        addComponent(title);

        container = new BeanItemContainer<>(Assignment.class);

        table = new Table("", container) {{
            setSizeFull();
            setSelectable(true);
            setImmediate(true);
            setVisibleColumns(
                    Assignment.PROP_ID,
                    Assignment.PROP_OPENED,
                    Assignment.PROP_TYPE,
                    Assignment.PROP_MUNICIPALITY,
                    Assignment.PROP_ADDRESS
            );
            setConverter(Assignment.PROP_OPENED, DateToStringConverter.dateTime());
            setConverter(Assignment.PROP_TYPE, assignmentTypeToStringConverter);
            setConverter(Assignment.PROP_MUNICIPALITY, municipalityToStringConverter);
            setColumnHeader(Assignment.PROP_ID, "Number");
            setColumnHeader(Assignment.PROP_OPENED, "Opened");
            setColumnHeader(Assignment.PROP_TYPE, "Type");
            setColumnHeader(Assignment.PROP_MUNICIPALITY, "Municipality");
            setColumnHeader(Assignment.PROP_ADDRESS, "Address");

            setSortEnabled(true);
            setSortContainerPropertyId(Assignment.PROP_ID);
            addValueChangeListener(AssignmentTableView.this::updateButtonStates);
        }};
        addComponent(table);
        setExpandRatio(table, 1f);

        addComponent(openAssignment = new Button("Open Assignment", AssignmentTableView.this::openAssignment) {{
            setDisableOnClick(true);
            setEnabled(false);
        }});
        setComponentAlignment(openAssignment, Alignment.BOTTOM_RIGHT);
        eventBus.subscribe(this);
        refresh();
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this);
    }

    @EventBusListenerMethod
    private void onAssignmentEvent(AssignmentEvent event) {
        refresh();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void refresh() {
        container.removeAllItems();
        container.addAll(assignmentService.findOpenAssignments());
    }

    private void openAssignment(Button.ClickEvent clickEvent) {
        getSelection().ifPresent((selection) -> AssignmentView.openAssignment(selection.getId()));
        openAssignment.setEnabled(true);
    }

    private void updateButtonStates(Property.ValueChangeEvent valueChangeEvent) {
        openAssignment.setEnabled(getSelection().isPresent());
    }

    private Optional<Assignment> getSelection() {
        return Optional.ofNullable((Assignment) table.getValue());
    }
}
