package net.pkhsolutions.idispatch.ui.dws.assignments;


import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
public class ShowAssignmentTableCommand implements MenuBar.Command {
    @Override
    public void menuSelected(MenuBar.MenuItem selectedItem) {
        UI.getCurrent().getNavigator().navigateTo(AssignmentTableView.VIEW_NAME);
    }
}
