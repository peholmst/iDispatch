package net.pkhsolutions.idispatch.ui.admin.resources;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.repository.ResourceRepository;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminUI;
import net.pkhsolutions.idispatch.ui.common.resources.ResourceTypeToStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

@VaadinView(name = ResourcesView.VIEW_NAME, ui = AdminUI.class)
@UIScope
public class ResourcesView extends AbstractCrudView<Resource, ResourceRepository> {

    // TODO Internationalize

    public static final String VIEW_NAME = "resources";

    @Autowired
    ResourceRepository repository;

    @Autowired
    ResourceTypeToStringConverter resourceTypeToStringConverter;

    @Override
    protected String getTitle() {
        return "Resources";
    }

    @Override
    protected ResourceRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }

    @Override
    protected void openCreateWindow(SaveCallback<Resource> saveCallback) {
        openWindow("Add Resource", createForm(ResourceForm.class, new Resource(), saveCallback));
    }

    @Override
    protected void openEditWindow(Resource entity, SaveCallback<Resource> saveCallback) {
        openWindow("Edit Resource", createForm(ResourceForm.class, entity, saveCallback));
    }

    @Override
    protected void configureTable(Table table) {
        table.setVisibleColumns(Resource.PROP_CALL_SIGN, Resource.PROP_TYPE);
        table.setConverter(Resource.PROP_TYPE, resourceTypeToStringConverter);
    }
}
