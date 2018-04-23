package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.entity.ResourceStatus;
import net.pkhsolutions.idispatch.ui.dws.DwsTheme;
import net.pkhsolutions.idispatch.ui.dws.DwsUI;
import net.pkhsolutions.idispatch.ui.dws.assignments.AssignmentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import java.util.Optional;

/**
 * View for showing and modifying resource states in a table.
 */
@VaadinView(name = ResourceTableView.VIEW_NAME, ui = DwsUI.class)
@UIScope
class ResourceTableView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "resourceTable";

    @Autowired
    ResourceStatusContainer container;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChangeResourceStatusWindow changeResourceStatusWindow;

    private Table table;
    private Button openAssignment;
    private Button changeState;

    @PostConstruct
    void init() {
        // TODO Internationalize
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final Label title = new Label("Resource Overview");
        title.addStyleName(DwsTheme.LABEL_H1);
        addComponent(title);

        table = new AbstractResourceStatusTable(container, applicationContext) {{
            setSizeFull();
            setSelectable(true);
            setImmediate(true);
            setVisibleColumns(
                    ResourceStatus.PROP_RESOURCE,
                    ResourceStatusContainer.NESTPROP_RESOURCE_TYPE,
                    ResourceStatus.PROP_STATE,
                    ResourceStatus.PROP_TIMESTAMP,
                    ResourceStatusContainer.NESTPROP_ASSIGNMENT_ID,
                    ResourceStatusContainer.NESTPROP_ASSIGNMENT_TYPE,
                    ResourceStatusContainer.NESTPROP_ASSIGNMENT_MUNICIPALITY,
                    ResourceStatusContainer.NESTPROP_ASSIGNMENT_ADDRESS
            );
            setSortEnabled(true);
            setSortContainerPropertyId(ResourceStatus.PROP_RESOURCE);
            addValueChangeListener(ResourceTableView.this::updateButtonStates);
        }};
        addComponent(table);
        setExpandRatio(table, 1f);

        final HorizontalLayout buttons = new HorizontalLayout() {{
            setSpacing(true);
            addComponent(changeState = new Button("Change State", ResourceTableView.this::changeState) {{
                setDisableOnClick(true);
                setEnabled(false);
            }});
            addComponent(openAssignment = new Button("Open Assignment", ResourceTableView.this::openAssignment) {{
                setDisableOnClick(true);
                setEnabled(false);
            }});
        }};
        addComponent(buttons);
        setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
    }

    private Optional<ResourceStatus> getSelection() {
        return Optional.ofNullable((ResourceStatus) table.getValue());
    }

    private void changeState(Button.ClickEvent clickEvent) {
        getSelection().ifPresent((selection) -> changeResourceStatusWindow.open(getUI(), selection.getResource()));
        changeState.setEnabled(true);
    }

    private void openAssignment(Button.ClickEvent clickEvent) {
        getSelection()
                .filter((selection) -> selection.getAssignment() != null)
                .ifPresent((selection) -> AssignmentView.openAssignment(selection.getAssignment().getId()));
        openAssignment.setEnabled(true);
    }

    private void updateButtonStates(Property.ValueChangeEvent valueChangeEvent) {
        boolean hasSelection = getSelection().isPresent();
        changeState.setEnabled(hasSelection);
        openAssignment.setEnabled(hasSelection && getSelection().get().getAssignment() != null);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        container.refresh();
    }

    @Override
    public void attach() {
        super.attach();
        container.attachedToUI(getUI());
    }

    @Override
    public void detach() {
        container.detachedFromUI(getUI());
        super.detach();
    }
}
