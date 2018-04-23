package net.pkhsolutions.idispatch.ui.admin.destinations;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import net.pkhsolutions.idispatch.entity.SmsDestination;
import net.pkhsolutions.idispatch.ui.admin.AdminTheme;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class SmsDestinationForm extends AbstractDestinationForm<SmsDestination> {

    @PropertyId("phoneNumbers")
    private TextArea phoneNumbers;

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        super.createAndAddFields(formLayout);

        phoneNumbers = new TextArea("Phone Numbers");
        phoneNumbers.setWidth("300px");
        phoneNumbers.setHeight("100px");
        phoneNumbers.setConverter(new PhoneNumbersToStringConverter());
        formLayout.addComponent(phoneNumbers);

        final Label info = new Label("Use commas or line breaks to separate numbers.<br/>Remember to add the +358 prefix!");
        info.setContentMode(ContentMode.HTML);
        info.addStyleName(AdminTheme.LABEL_SMALL);
        formLayout.addComponent(info);
    }

    @Override
    protected Class<SmsDestination> getEntityType() {
        return SmsDestination.class;
    }
}
