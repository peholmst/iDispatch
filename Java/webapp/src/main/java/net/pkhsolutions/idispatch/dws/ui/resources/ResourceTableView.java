package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.data.Property;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer;
import net.pkhsolutions.idispatch.domain.resources.ResourceStatus;
import net.pkhsolutions.idispatch.dws.ui.DwsTheme;
import net.pkhsolutions.idispatch.dws.ui.DwsUI;
import net.pkhsolutions.idispatch.dws.ui.tickets.TicketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Optional;

/**
 * View for showing and modifying resource states in a table.
 */
@VaadinView(name = ResourceTableView.VIEW_NAME, ui = DwsUI.class)
@UIScope
public class ResourceTableView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "resourceTable";

    @Autowired
    ResourceStatusContainer container;
    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ChangeResourceStatusWindow changeResourceStatusWindow;

    private Table table;
    private Button openTicket;
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
                    CurrentResourceStateContainer.NESTPROP_RESOURCE_TYPE,
                    ResourceStatus.PROP_STATE,
                    ResourceStatus.PROP_TIMESTAMP,
                    CurrentResourceStateContainer.NESTPROP_TICKET_ID,
                    CurrentResourceStateContainer.NESTPROP_TICKET_TYPE,
                    CurrentResourceStateContainer.NESTPROP_TICKET_MUNICIPALITY,
                    CurrentResourceStateContainer.NESTPROP_TICKET_ADDRESS
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
            addComponent(openTicket = new Button("Open Ticket", ResourceTableView.this::openTicket) {{
                setDisableOnClick(true);
                setEnabled(false);
            }});
        }};
        addComponent(buttons);
        setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        eventBus.subscribe(container);
    }

    private Optional<ResourceStatus> getSelection() {
        return Optional.ofNullable((ResourceStatus) table.getValue());
    }

    private void changeState(Button.ClickEvent clickEvent) {
        getSelection().ifPresent((selection) -> changeResourceStatusWindow.open(getUI(), selection.getResource()));
        changeState.setEnabled(true);
    }

    private void openTicket(Button.ClickEvent clickEvent) {
        getSelection()
                .filter((selection) -> selection.getTicket() != null)
                .ifPresent((selection) -> TicketView.openTicket(selection.getTicket().getId()));
        openTicket.setEnabled(true);
    }

    private void updateButtonStates(Property.ValueChangeEvent valueChangeEvent) {
        boolean hasSelection = getSelection().isPresent();
        changeState.setEnabled(hasSelection);
        openTicket.setEnabled(hasSelection && getSelection().get().getTicket() != null);
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(container);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
