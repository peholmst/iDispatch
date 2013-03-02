package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.cdi.VaadinView;
import com.vaadin.cdi.component.JaasTools;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.TicketTypeEJB;
import net.pkhsolutions.idispatch.entity.TicketType;

/**
 *
 * @author Petter Holmström
 */
@VaadinView(value = TicketTypeView.VIEW_ID, rolesAllowed = Roles.ADMIN)
public class TicketTypeView extends AbstractMasterDataView<TicketType> {

    public static final String VIEW_ID = "ticketTypeMasterData";
    @Inject
    private TicketTypeEJB ticketType;
    @Inject
    private TicketTypeViewBundle bundle;

    @Override
    protected Backend<TicketType> getBackend() {
        return ticketType;
    }

    @Messages({
        @Message(key = "code", value = "Kod"),
        @Message(key = "descriptionFi", value = "Beskrivning (finska)"),
        @Message(key = "descriptionSv", value = "Beskrivning (svenska)")
    })
    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField code = new TextField(bundle.code());
        code.setWidth("100px");
        code.setNullRepresentation("");
        fieldGroup.bind(code, "code");
        formLayout.addComponent(code);

        TextField descriptionFi = new TextField(bundle.descriptionFi());
        descriptionFi.setWidth("200px");
        descriptionFi.setNullRepresentation("");
        fieldGroup.bind(descriptionFi, "descriptionFi");
        formLayout.addComponent(descriptionFi);

        TextField descriptionSv = new TextField(bundle.descriptionSv());
        descriptionSv.setWidth("200px");
        descriptionSv.setNullRepresentation("");
        fieldGroup.bind(descriptionSv, "descriptionSv");
        formLayout.addComponent(descriptionSv);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"code", "descriptionFi", "descriptionSv"});
        table.setColumnHeaders(new String[]{bundle.code(), bundle.descriptionFi(), bundle.descriptionSv()});
        table.setSortContainerPropertyId("code");
    }

    @Override
    protected Class<TicketType> getEntityClass() {
        return TicketType.class;
    }

    @Override
    protected TicketType createNewEntity() {
        return new TicketType.Builder().build();
    }

    @Override
    protected TicketType createCopyOfEntity(TicketType entity) {
        return new TicketType.Builder(entity).build();
    }

    @Message(key = "title", value = "Administrera uppdragstyper")
    @Override
    protected String getTitle() {
        return bundle.title();
    }

    public static class MenuItemRegistrar {

        @Inject
        TicketTypeViewBundle bundle;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            if (JaasTools.isUserInRole(Roles.ADMIN)) {
                event.getMenu().addMenuItem(bundle.title(), VIEW_ID, null, 5, "admin");
            }
        }
    }
}
