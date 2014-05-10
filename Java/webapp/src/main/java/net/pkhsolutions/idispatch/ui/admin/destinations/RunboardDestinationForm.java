package net.pkhsolutions.idispatch.ui.admin.destinations;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import net.pkhsolutions.idispatch.entity.RunboardDestination;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class RunboardDestinationForm extends AbstractDestinationForm<RunboardDestination> {

    @PropertyId("runboardKey")
    private TextField runboardKey;

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        super.createAndAddFields(formLayout);

        runboardKey = new TextField("Runboard Key");
        runboardKey.setWidth("300px");
        formLayout.addComponent(runboardKey);
    }

    @Override
    protected Class<RunboardDestination> getEntityType() {
        return RunboardDestination.class;
    }
}
