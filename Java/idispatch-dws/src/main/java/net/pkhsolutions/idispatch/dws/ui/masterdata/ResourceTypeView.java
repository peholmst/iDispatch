package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.vaadin.cdi.VaadinView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceTypeEJB;
import net.pkhsolutions.idispatch.entity.ResourceType;

/**
 *
 * @author peholmst
 */
@VaadinView(ResourceTypeView.VIEW_ID)
public class ResourceTypeView extends AbstractMasterDataView<ResourceType> {

    public static final String VIEW_ID = "resourceTypeMasterData";
    @Inject
    ResourceTypeEJB resourceType;

    @Override
    protected String getTitle() {
        return "Resource Type Master Data";
    }

    @Override
    protected Backend<ResourceType> getBackend() {
        return resourceType;
    }

    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField name = new TextField("Name");
        name.setWidth("200px");
        name.setNullRepresentation("");
        fieldGroup.bind(name, "name");
        formLayout.addComponent(name);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"name"});
    }

    @Override
    protected Class<ResourceType> getEntityClass() {
        return ResourceType.class;
    }

    @Override
    protected ResourceType createNewEntity() {
        return new ResourceType.Builder().build();
    }

    @Override
    protected ResourceType createCopyOfEntity(ResourceType entity) {
        return new ResourceType.Builder(entity).build();
    }

    public static class MenuItemRegistrar {

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem("Resource Type Master Data", VIEW_ID);
        }
    }
}
