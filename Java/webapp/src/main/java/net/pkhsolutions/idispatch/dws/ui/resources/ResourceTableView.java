package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateContainer;
import net.pkhsolutions.idispatch.common.ui.resources.CurrentResourceStateTable;
import net.pkhsolutions.idispatch.dws.ui.DwsTheme;
import net.pkhsolutions.idispatch.dws.ui.DwsUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusScope;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.navigator.VaadinView;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * View for showing and modifying resource states in a table.
 */
@VaadinView(name = ResourceTableView.VIEW_NAME, ui = DwsUI.class)
@UIScope
public class ResourceTableView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "resourceTable";

    @Autowired
    CurrentResourceStateContainer container;

    @Autowired
    CurrentResourceStateTable table;

    @Autowired
    @EventBusScope(EventScope.APPLICATION)
    EventBus eventBus;

    @PostConstruct
    void init() {
        // TODO Internationalize
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        final Label title = new Label("Resource Overview");
        title.addStyleName(DwsTheme.LABEL_H1);
        addComponent(title);

        table.setSizeFull();
        table.setContainerDataSource(container);
        addComponent(table);
        setExpandRatio(table, 1f);

        // TODO Controls for changing state and assigning resources to tickets

        eventBus.subscribe(container);
        container.refresh();
    }

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(container);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
}
