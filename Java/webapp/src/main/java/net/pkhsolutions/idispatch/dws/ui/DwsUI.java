package net.pkhsolutions.idispatch.dws.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.common.ui.ErrorView;
import net.pkhsolutions.idispatch.dws.ui.resources.ShowResourceTableCommand;
import net.pkhsolutions.idispatch.dws.ui.tickets.NewTicketCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Vaadin UI for the Dispatcher Workstation (DWS). This UI is used to create and update tickets,
 * dispatch and track/update the state of resources.
 */
@VaadinUI(path = "/dws")
@Theme(DwsTheme.THEME_NAME)
public class DwsUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Autowired
    NewTicketCommand newTicketCommand;

    @Autowired
    ShowResourceTableCommand showResourceTableCommand;

    private MenuBar menuBar;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        menuBar = new MenuBar();
        menuBar.setWidth("100%");
        root.addComponent(menuBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        viewContainer.addStyleName(DwsTheme.PANEL_LIGHT);
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1);

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(ErrorView.class);
        navigator.addProvider(viewProvider);

        addMenuItems(menuBar);
    }

    private void addMenuItems(MenuBar menuBar) {
        final MenuBar.MenuItem ticket = menuBar.addItem("Ticket", null);
        ticket.addItem("Open New Ticket", newTicketCommand);

        final MenuBar.MenuItem viewMenu = menuBar.addItem("View", null);
        viewMenu.addItem("Tickets", null);
        viewMenu.addItem("Resource Table", showResourceTableCommand);
        viewMenu.addSeparator();
        viewMenu.addItem("Open New Browser Window", null);

        final MenuBar.MenuItem accountMenu = menuBar.addItem("Account", null);
        accountMenu.addItem("Logout", null);
    }
}
