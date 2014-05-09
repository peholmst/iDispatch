package net.pkhsolutions.idispatch.ui.admin;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import net.pkhsolutions.idispatch.ui.admin.assignmenttypes.AssignmentTypesView;
import net.pkhsolutions.idispatch.ui.admin.destinations.DestinationsView;
import net.pkhsolutions.idispatch.ui.admin.municipalities.MunicipalitiesView;
import net.pkhsolutions.idispatch.ui.admin.resources.ResourcesView;
import net.pkhsolutions.idispatch.ui.admin.resourcetypes.ResourceTypesView;
import net.pkhsolutions.idispatch.ui.common.ErrorView;
import net.pkhsolutions.idispatch.ui.dws.DwsTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

@VaadinUI(path = "/admin")
public class AdminUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

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

        setPollInterval(300);
    }

    private void addMenuItems(MenuBar menuBar) {
        final MenuBar.MenuItem viewMenu = menuBar.addItem("View", null);
        viewMenu.addItem("Resources", selectedItem -> navigateToView(ResourcesView.VIEW_NAME));
        viewMenu.addItem("Destinations", selectedItem -> navigateToView(DestinationsView.VIEW_NAME));
        viewMenu.addSeparator();
        viewMenu.addItem("Assignment Types", selectedItem -> navigateToView(AssignmentTypesView.VIEW_NAME));
        viewMenu.addItem("Resource Types", selectedItem -> navigateToView(ResourceTypesView.VIEW_NAME));
        viewMenu.addSeparator();
        viewMenu.addItem("Municipalities", selectedItem -> navigateToView(MunicipalitiesView.VIEW_NAME));
    }

    private void navigateToView(String viewId) {
        getUI().getNavigator().navigateTo(viewId);
    }
}
