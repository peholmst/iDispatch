package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.vaadin.cdi.VaadinView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceTypeEJB;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceType;

/**
 *
 * @author peholmst
 */
@VaadinView(ResourceView.VIEW_ID)
public class ResourceView extends AbstractMasterDataView<Resource> {

    public static final String VIEW_ID = "resourceMasterData";
    @Inject
    ResourceEJB resource;
    @Inject
    ResourceTypeEJB resourceType;

    @Override
    protected String getTitle() {
        return "Resource Master Data";
    }

    @Override
    protected Backend<Resource> getBackend() {
        return resource;
    }

    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField callSign = new TextField("Call Sign");
        callSign.setWidth("100px");
        callSign.setNullRepresentation("");
        fieldGroup.bind(callSign, "callSign");
        formLayout.addComponent(callSign);

        ComboBox type = new ComboBox("Type", new BeanItemContainer<>(resourceType.findAll()));
        type.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        type.setItemCaptionPropertyId("name");
        fieldGroup.bind(type, "resourceType");
        formLayout.addComponent(type);
    }

    @Override
    protected void setUpTable(Table table) {
        table.addGeneratedColumn("typeName", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                ResourceType rt = (ResourceType) source.getItem(itemId).getItemProperty("resourceType").getValue();
                return rt.getName();
            }
        });
        table.setVisibleColumns(new String[]{"callSign", "typeName"});
        table.setColumnHeaders(new String[]{"Call Sign", "Type"});
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }

    @Override
    protected Resource createNewEntity() {
        return new Resource.Builder().build();
    }

    @Override
    protected Resource createCopyOfEntity(Resource entity) {
        return new Resource.Builder(entity).build();
    }

    public static class MenuItemRegistrar {

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem("Resource Master Data", VIEW_ID);
        }
    }
}
