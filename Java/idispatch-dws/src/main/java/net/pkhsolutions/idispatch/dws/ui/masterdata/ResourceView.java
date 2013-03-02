package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.cdi.VaadinView;
import com.vaadin.cdi.component.JaasTools;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.util.Locale;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceEJB;
import net.pkhsolutions.idispatch.ejb.masterdata.ResourceTypeEJB;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceType;
import org.apache.commons.lang.StringUtils;

@VaadinView(value = ResourceView.VIEW_ID, rolesAllowed = Roles.ADMIN)
public class ResourceView extends AbstractMasterDataView<Resource> {

    public static final String VIEW_ID = "resourceMasterData";
    @Inject
    private ResourceEJB resource;
    @Inject
    private ResourceTypeEJB resourceType;
    @Inject
    private ResourceViewBundle bundle;
    @Inject
    private I18N i18n;

    @Message(key = "title", value = "Administrera resurser")
    @Override
    protected String getTitle() {
        return bundle.title();
    }

    @Override
    protected Backend<Resource> getBackend() {
        return resource;
    }

    @Messages({
        @Message(key = "callSign", value = "Anropsnamn"),
        @Message(key = "resourceType", value = "Typ"),
        @Message(key = "active", value = "Aktiv")
    })
    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField callSign = new TextField(bundle.callSign());
        callSign.setWidth("100px");
        callSign.setNullRepresentation("");
        fieldGroup.bind(callSign, "callSign");
        formLayout.addComponent(callSign);

        ComboBox type = new ComboBox(bundle.resourceType(), new BeanItemContainer<>(ResourceType.class, resourceType.findAll()));
        type.setItemCaptionMode(AbstractSelect.ItemCaptionMode.PROPERTY);
        type.setItemCaptionPropertyId("name" + StringUtils.capitalize(i18n.getLocale().getLanguage()));
        fieldGroup.bind(type, "resourceType");
        formLayout.addComponent(type);

        CheckBox active = new CheckBox(bundle.active());
        fieldGroup.bind(active, "active");
        formLayout.addComponent(active);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"callSign", "resourceType"});
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                Resource resource = (Resource) itemId;
                if (!resource.isActive()) {
                    return "not-active";
                } else {
                    return null;
                }
            }
        });
        table.setColumnHeaders(new String[]{bundle.callSign(), bundle.resourceType()});
        table.setSortContainerPropertyId("callSign");
        table.setConverter("resourceType", new Converter<String, ResourceType>() {
            @Override
            public ResourceType convertToModel(String value, Locale locale) throws Converter.ConversionException {
                return null; // This is a read-only converter
            }

            @Override
            public String convertToPresentation(ResourceType value, Locale locale) throws Converter.ConversionException {
                return value.getName(i18n.getLocale());
            }

            @Override
            public Class<ResourceType> getModelType() {
                return ResourceType.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
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

        @Inject
        ResourceViewBundle bundle;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            if (JaasTools.isUserInRole(Roles.ADMIN)) {
                event.getMenu().addMenuItem(bundle.title(), VIEW_ID, null, 4, "admin");
            }
        }
    }
}
