package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.common.Roles;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.DispatchNotificationReceiverEJB;
import net.pkhsolutions.idispatch.entity.DispatchNotificationReceiver;

/**
 *
 * @author Petter Holmström
 */
@CDIView(value = ReceiverView.VIEW_ID)
@RolesAllowed(Roles.ADMIN)
public class ReceiverView extends AbstractMasterDataView<DispatchNotificationReceiver> {

    public static final String VIEW_ID = "dispatchNotificationReceiverMasterData";
    @Inject
    private DispatchNotificationReceiverEJB receiver;
    @Inject
    private ReceiverViewBundle bundle;

    @Override
    protected Backend<DispatchNotificationReceiver> getBackend() {
        return receiver;
    }

    @Messages({
        @Message(key = "receiverId", value = "Mottagar-ID"),
        @Message(key = "securityCode", value = "Säkerhetskod"),
        @Message(key = "active", value = "Aktiv")
    })
    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField receiverId = new TextField(bundle.receiverId());
        receiverId.setWidth("200px");
        receiverId.setNullRepresentation("");
        fieldGroup.bind(receiverId, "receiverId");
        formLayout.addComponent(receiverId);

        TextField securityCode = new TextField(bundle.securityCode());
        securityCode.setWidth("200px");
        securityCode.setNullRepresentation("");
        fieldGroup.bind(securityCode, "securityCode");
        formLayout.addComponent(securityCode);

        CheckBox active = new CheckBox(bundle.active());
        fieldGroup.bind(active, "active");
        formLayout.addComponent(active);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"receiverId", "securityCode"});
        table.setColumnHeaders(new String[]{bundle.receiverId(), bundle.securityCode()});
        table.setCellStyleGenerator(new Table.CellStyleGenerator() {
            @Override
            public String getStyle(Table source, Object itemId, Object propertyId) {
                DispatchNotificationReceiver receiver = (DispatchNotificationReceiver) itemId;
                if (!receiver.isActive()) {
                    return "not-active";
                } else {
                    return null;
                }
            }
        });

        table.setSortContainerPropertyId("receiverId");
    }

    @Override
    protected Class<DispatchNotificationReceiver> getEntityClass() {
        return DispatchNotificationReceiver.class;
    }

    @Override
    protected DispatchNotificationReceiver createNewEntity() {
        return new DispatchNotificationReceiver.Builder().withGeneratedSecurityCode().build();
    }

    @Override
    protected DispatchNotificationReceiver createCopyOfEntity(DispatchNotificationReceiver entity) {
        return new DispatchNotificationReceiver.Builder(entity).build();
    }

    @Message(key = "title", value = "Administrera larmmottagare")
    @Override
    protected String getTitle() {
        return bundle.title();
    }

    public static class MenuItemRegistrar {

        @Inject
        ReceiverViewBundle bundle;

        @Inject
        AccessControl accessControl;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            if (accessControl.isUserInRole(Roles.ADMIN)) {
                event.getMenu().addMenuItem(bundle.title(), VIEW_ID, null, 6, "admin");
            }
        }
    }
}
