package net.pkhsolutions.idispatch.ui.admin.resourcetypes;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import net.pkhsolutions.idispatch.entity.ResourceType;
import net.pkhsolutions.idispatch.ui.admin.AbstractForm;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class ResourceTypeForm extends AbstractForm<ResourceType> {

    // TODO Internationalize

    @PropertyId(ResourceType.PROP_CODE)
    private TextField code;

    @PropertyId(ResourceType.PROP_DESCRIPTION)
    private TextField description;

    @Override
    protected Class<ResourceType> getEntityType() {
        return ResourceType.class;
    }

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        code = new TextField("Code");
        formLayout.addComponent(code);

        description = new TextField("Description");
        formLayout.addComponent(description);
    }
}
