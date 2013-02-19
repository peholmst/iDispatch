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
import net.pkhsolutions.idispatch.ejb.masterdata.TicketTypeEJB;
import net.pkhsolutions.idispatch.entity.TicketType;

/**
 *
 * @author Petter Holmström
 */
@VaadinView(TicketTypeView.VIEW_ID)
public class TicketTypeView extends AbstractMasterDataView<TicketType> {

    public static final String VIEW_ID = "ticketTypeMasterData";
    @Inject
    TicketTypeEJB ticketType;

    @Override
    protected Backend<TicketType> getBackend() {
        return ticketType;
    }

    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField code = new TextField("Code");
        code.setWidth("100px");
        code.setNullRepresentation("");
        fieldGroup.bind(code, "code");
        formLayout.addComponent(code);

        TextField description = new TextField("Description");
        description.setWidth("200px");
        description.setNullRepresentation("");
        fieldGroup.bind(description, "description");
        formLayout.addComponent(description);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"code", "description"});
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

    @Override
    protected String getTitle() {
        return "Ticket Type Master Data";
    }

    public static class MenuItemRegistrar {

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem("Ticket Type Master Data", VIEW_ID);
        }
    }
}
