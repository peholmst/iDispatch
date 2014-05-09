package net.pkhsolutions.idispatch.ui.admin.destinations;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.boundary.DestinationManagementService;
import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.RunboardDestination;
import net.pkhsolutions.idispatch.entity.SmsDestination;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

@VaadinView(name = DestinationsView.VIEW_NAME, ui = AdminUI.class)
@UIScope
public class DestinationsView extends AbstractCrudView<Destination, DestinationManagementService> {

    // TODO Internationalize

    public static final String VIEW_NAME = "destinations";

    @Autowired
    DestinationManagementService service;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    protected String getTitle() {
        return "Destinations";
    }

    @Override
    protected DestinationManagementService getManagementService() {
        return service;
    }

    @Override
    protected Class<Destination> getEntityClass() {
        return Destination.class;
    }

    @Override
    protected void openCreateWindow(SaveCallback<Destination> saveCallback) {
        final SmsDestinationForm form = createSmsDestinationForm();
        form.init(new SmsDestination(), saveCallback);
        openWindow("Add SMS Destination", form);
    }

    @Override
    protected void openEditWindow(Destination entity, SaveCallback<Destination> saveCallback) {
        if (entity instanceof SmsDestination) {
            final SmsDestinationForm form = createSmsDestinationForm();
            form.init((SmsDestination) entity, saveCallback);
            openWindow("Edit SMS Destination", form);
        }
    }

    private SmsDestinationForm createSmsDestinationForm() {
        return applicationContext.getBean(SmsDestinationForm.class);
    }

    @Override
    protected void configureTable(Table table) {
        table.addGeneratedColumn("TYPE", this::getDestinationType);
        table.setVisibleColumns("TYPE", Destination.PROP_RESOURCES);
        table.setConverter(Destination.PROP_RESOURCES, new ResourcesToStringConverter());
    }

    private String getDestinationType(Table source, Object itemId, Object columnId) {
        if (itemId instanceof RunboardDestination) {
            return "Runboard";
        } else if (itemId instanceof SmsDestination) {
            return "SMS";
        } else {
            return "Unknown";
        }
    }
}
