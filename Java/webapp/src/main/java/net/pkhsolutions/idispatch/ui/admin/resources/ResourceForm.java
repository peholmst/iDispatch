package net.pkhsolutions.idispatch.ui.admin.resources;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.ResourceType;
import net.pkhsolutions.idispatch.entity.repository.ResourceTypeRepository;
import net.pkhsolutions.idispatch.ui.admin.AbstractForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
public class ResourceForm extends AbstractForm<Resource> {

    // TODO Internationalize

    @Autowired
    ResourceTypeRepository resourceTypeRepository;

    @PropertyId(Resource.PROP_TYPE)
    private ComboBox type;

    @PropertyId(Resource.PROP_ACTIVE)
    private CheckBox active;

    @PropertyId(Resource.PROP_CALL_SIGN)
    private TextField callSign;

    @Override
    protected Class<Resource> getEntityType() {
        return Resource.class;
    }

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        type = new ComboBox("Type", new BeanItemContainer<>(ResourceType.class, resourceTypeRepository.findAll()));
        type.setItemCaptionPropertyId(ResourceType.PROP_DESCRIPTION);
        formLayout.addComponent(type);

        callSign = new TextField("Call Sign");
        formLayout.addComponent(callSign);

        active = new CheckBox("This resource is active");
        formLayout.addComponent(active);
    }
}
