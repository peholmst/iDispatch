package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
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
    @Inject
    ResourceTypeViewBundle bundle;

    @Message(key = "title", value = "Resurstyper")
    @Override
    protected String getTitle() {
        return bundle.title();
    }

    @Override
    protected Backend<ResourceType> getBackend() {
        return resourceType;
    }

    @Messages({
        @Message(key = "nameFi", value = "Namn (finska)"),
        @Message(key = "nameSv", value = "Namn (svenska)")
    })
    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField nameFi = new TextField(bundle.nameFi());
        nameFi.setWidth("200px");
        nameFi.setNullRepresentation("");
        fieldGroup.bind(nameFi, "nameFi");
        formLayout.addComponent(nameFi);

        TextField nameSv = new TextField(bundle.nameSv());
        nameSv.setWidth("200px");
        nameSv.setNullRepresentation("");
        fieldGroup.bind(nameSv, "nameSv");
        formLayout.addComponent(nameSv);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"nameFi", "nameSv"});
        table.setColumnHeaders(new String[]{bundle.nameFi(), bundle.nameSv()});
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

        @Inject
        ResourceTypeViewBundle bundle;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem(bundle.title(), VIEW_ID);
        }
    }
}
