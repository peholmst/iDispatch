package net.pkhsolutions.idispatch.ui.admin.resourcetypes;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.boundary.ResourceTypeManagementService;
import net.pkhsolutions.idispatch.entity.ResourceType;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

@VaadinView(name = ResourceTypesView.VIEW_NAME, ui = AdminUI.class)
@UIScope
public class ResourceTypesView extends AbstractCrudView<ResourceType, ResourceTypeManagementService> {

    // TODO Internationalize

    public static final String VIEW_NAME = "resourceTypes";

    @Autowired
    ResourceTypeManagementService service;

    @Override
    protected String getTitle() {
        return "Resource Types";
    }

    @Override
    protected ResourceTypeManagementService getManagementService() {
        return service;
    }

    @Override
    protected Class<ResourceType> getEntityClass() {
        return ResourceType.class;
    }

    @Override
    protected void openCreateWindow(SaveCallback<ResourceType> saveCallback) {
        openWindow("Add Resource Type", createForm(ResourceTypeForm.class, new ResourceType(), saveCallback));
    }

    @Override
    protected void openEditWindow(ResourceType entity, SaveCallback<ResourceType> saveCallback) {
        openWindow("Edit Resource Type", createForm(ResourceTypeForm.class, entity, saveCallback));
    }

    @Override
    protected void configureTable(Table table) {
        table.setVisibleColumns(ResourceType.PROP_CODE, ResourceType.PROP_DESCRIPTION);
    }
}
