package net.pkhsolutions.idispatch.ui.admin.destinations;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TwinColSelect;
import net.pkhsolutions.idispatch.entity.Destination;
import net.pkhsolutions.idispatch.entity.Resource;
import net.pkhsolutions.idispatch.entity.repository.ResourceRepository;
import net.pkhsolutions.idispatch.ui.admin.AbstractForm;
import org.springframework.beans.factory.annotation.Autowired;

abstract class AbstractDestinationForm<D extends Destination> extends AbstractForm<D> {

    // TODO Internationalize

    @Autowired
    ResourceRepository resourceRepository;

    @PropertyId(Destination.PROP_RESOURCES)
    private TwinColSelect resources;

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        resources = new TwinColSelect("Resources", new BeanItemContainer<>(Resource.class, resourceRepository.findByActiveTrueOrderByCallSignAsc()));
        resources.setWidth("300px");
        resources.setItemCaptionPropertyId(Resource.PROP_CALL_SIGN);
        formLayout.addComponent(resources);
    }
}
