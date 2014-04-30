package net.pkhsolutions.idispatch.dws.ui.resources;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

/**
 * Menu bar command that navigates to the {@link net.pkhsolutions.idispatch.dws.ui.resources.ResourceTableView}.
 */
@VaadinComponent
@Scope("prototype")
public class ShowResourceTableCommand implements MenuBar.Command {
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        UI.getCurrent().getNavigator().navigateTo(ResourceTableView.VIEW_NAME);
    }
}
