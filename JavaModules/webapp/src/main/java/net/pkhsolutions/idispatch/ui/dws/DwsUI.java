package net.pkhsolutions.idispatch.ui.dws;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.ui.common.ErrorView;
import net.pkhsolutions.idispatch.ui.dws.assignments.OpenAssignmentCommand;
import net.pkhsolutions.idispatch.ui.dws.assignments.ShowAssignmentTableCommand;
import net.pkhsolutions.idispatch.ui.dws.resources.ShowResourceTableCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

/**
 * Vaadin UI for the Dispatcher Workstation (DWS). This UI is used to create and update assignments,
 * dispatch and track/update the state of resources.
 */
@VaadinUI(path = "/dws")
@Theme(DwsTheme.THEME_NAME)
@Widgetset("net.pkhsolutions.idispatch.ui.dws.DwsWidgetset")
public class DwsUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Autowired
    OpenAssignmentCommand openAssignmentCommand;

    @Autowired
    ShowResourceTableCommand showResourceTableCommand;

    @Autowired
    ShowAssignmentTableCommand showAssignmentTableCommand;

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

        setPollInterval(400);
    }

    private void addMenuItems(MenuBar menuBar) {
        final MenuBar.MenuItem assignment = menuBar.addItem("Assignment", null);
        assignment.addItem("Open New Assignment", openAssignmentCommand);

        final MenuBar.MenuItem viewMenu = menuBar.addItem("View", null);
        viewMenu.addItem("Assignment Table", showAssignmentTableCommand);
        viewMenu.addItem("Resource Table", showResourceTableCommand);

//        final MenuBar.MenuItem accountMenu = menuBar.addItem("Account", null);
//        accountMenu.addItem("Logout", null);
    }
}
