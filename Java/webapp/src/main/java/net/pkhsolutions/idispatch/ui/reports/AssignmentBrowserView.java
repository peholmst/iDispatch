package net.pkhsolutions.idispatch.ui.reports;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.boundary.AssignmentService;
import net.pkhsolutions.idispatch.entity.Assignment;
import net.pkhsolutions.idispatch.ui.admin.AdminTheme;
import net.pkhsolutions.idispatch.ui.common.DateToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.AssignmentTypeToStringConverter;
import net.pkhsolutions.idispatch.ui.common.resources.MunicipalityToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Optional;

@VaadinView(name = "", ui = ReportsUI.class)
@UIScope
public class AssignmentBrowserView extends VerticalLayout implements View {

    @Autowired
    AssignmentService assignmentService;

    @Autowired
    AssignmentTypeToStringConverter assignmentTypeToStringConverter;

    @Autowired
    MunicipalityToStringConverter municipalityToStringConverter;

    private BeanItemContainer<Assignment> container;

    private Table table;

    private Button refresh;

    private Button openReport;

    @PostConstruct
    void init() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        setSpacing(true);

        final Label title = new Label("Closed Assignments");
        title.addStyleName(AdminTheme.LABEL_H1);
        addComponent(title);

        container = new BeanItemContainer<>(Assignment.class);
        table = new Table();
        table.setImmediate(true);
        table.setSelectable(true);
        table.setSizeFull();
        table.addValueChangeListener(event -> updateButtonStates());
        table.setContainerDataSource(container);
        table.setVisibleColumns(
                Assignment.PROP_ID,
                Assignment.PROP_OPENED,
                Assignment.PROP_CLOSED,
                Assignment.PROP_TYPE,
                Assignment.PROP_MUNICIPALITY,
                Assignment.PROP_ADDRESS
        );
        table.setConverter(Assignment.PROP_OPENED, DateToStringConverter.dateTime());
        table.setConverter(Assignment.PROP_CLOSED, DateToStringConverter.dateTime());
        table.setConverter(Assignment.PROP_TYPE, assignmentTypeToStringConverter);
        table.setConverter(Assignment.PROP_MUNICIPALITY, municipalityToStringConverter);
        table.setColumnHeader(Assignment.PROP_ID, "Number");
        table.setColumnHeader(Assignment.PROP_OPENED, "Opened");
        table.setColumnHeader(Assignment.PROP_CLOSED, "Closed");
        table.setColumnHeader(Assignment.PROP_TYPE, "Type");
        table.setColumnHeader(Assignment.PROP_MUNICIPALITY, "Municipality");
        table.setColumnHeader(Assignment.PROP_ADDRESS, "Address");
        table.setSortEnabled(true);
        table.setSortContainerPropertyId(Assignment.PROP_ID);
        addComponent(table);
        setExpandRatio(table, 1f);

        final HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        addComponent(buttons);

        buttons.addComponent(refresh = new Button("Refresh", this::onRefresh));
        buttons.setExpandRatio(refresh, 1f);
        refresh.setDisableOnClick(true);

        buttons.addComponent(openReport = new Button("Open Report", this::onOpenReport));
        buttons.setComponentAlignment(openReport, Alignment.MIDDLE_RIGHT);
        openReport.setDisableOnClick(true);

        updateButtonStates();
    }

    private void onRefresh(Button.ClickEvent clickEvent) {
        try {
            refresh();
        } finally {
            refresh.setEnabled(true);
        }
    }

    private void onOpenReport(Button.ClickEvent clickEvent) {
        Assignment selectedAssignment = (Assignment) table.getValue();
        if (selectedAssignment != null) {
            getUI().getNavigator().navigateTo(ReportView.VIEW_NAME + "/" + selectedAssignment.getId());
        }
    }

    private void updateButtonStates() {
        openReport.setEnabled(table.getValue() != null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        refresh();
    }

    private void refresh() {
        container.removeAllItems();
        container.addAll(assignmentService.findClosedAssignments(Optional.empty()));
        table.setValue(null);
    }
}
