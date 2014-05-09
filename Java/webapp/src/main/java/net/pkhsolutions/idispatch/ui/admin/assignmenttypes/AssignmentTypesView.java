package net.pkhsolutions.idispatch.ui.admin.assignmenttypes;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.boundary.AssignmentTypeManagementService;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

@VaadinView(name = AssignmentTypesView.VIEW_NAME, ui = AdminUI.class)
@UIScope
public class AssignmentTypesView extends AbstractCrudView<AssignmentType, AssignmentTypeManagementService> {

    // TODO Internationalize

    public static final String VIEW_NAME = "assignmentTypes";

    @Autowired
    AssignmentTypeManagementService service;

    @Override
    protected String getTitle() {
        return "Assignment Types";
    }

    @Override
    protected AssignmentTypeManagementService getManagementService() {
        return service;
    }

    @Override
    protected Class<AssignmentType> getEntityClass() {
        return AssignmentType.class;
    }

    @Override
    protected void openCreateWindow(SaveCallback<AssignmentType> saveCallback) {
        openWindow("Add Assignment Type", createForm(AssignmentTypeForm.class, new AssignmentType(), saveCallback));
    }

    @Override
    protected void openEditWindow(AssignmentType entity, SaveCallback<AssignmentType> saveCallback) {
        openWindow("Edit Assignment Type", createForm(AssignmentTypeForm.class, entity, saveCallback));
    }

    @Override
    protected void configureTable(Table table) {
        table.setVisibleColumns(AssignmentType.PROP_CODE, AssignmentType.PROP_DESCRIPTION);
    }
}
