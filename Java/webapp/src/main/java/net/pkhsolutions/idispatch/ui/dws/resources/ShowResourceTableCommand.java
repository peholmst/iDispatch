package net.pkhsolutions.idispatch.ui.dws.resources;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

/**
 * Menu bar command that navigates to the {@link net.pkhsolutions.idispatch.ui.dws.resources.ResourceTableView}.
 */
@VaadinComponent
@Scope("prototype")
public class ShowResourceTableCommand implements MenuBar.Command {
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        UI.getCurrent().getNavigator().navigateTo(ResourceTableView.VIEW_NAME);
    }
}
