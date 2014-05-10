package net.pkhsolutions.idispatch.ui.admin.destinations;


import com.vaadin.ui.TabSheet;
import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.RunboardDestination;
import net.pkhsolutions.idispatch.entity.SmsDestination;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class NewDestinationForm extends TabSheet {

    @Autowired
    SmsDestinationForm smsDestinationForm;

    @Autowired
    RunboardDestinationForm runboardDestinationForm;

    void init(AbstractCrudView.SaveCallback<Destination> saveCallback) {
        setSizeUndefined();
        addStyleName(AdminTheme.TABSHEET_BORDERLESS);
        smsDestinationForm.init(new SmsDestination(), saveCallback);
        runboardDestinationForm.init(new RunboardDestination(), saveCallback);

        addTab(smsDestinationForm, "SMS Destination");
        addTab(runboardDestinationForm, "Runboard Destination");
    }

}
