package net.pkhsolutions.idispatch.ui.admin.municipalities;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.ui.admin.AbstractForm;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class MunicipalityForm extends AbstractForm<Municipality> {

    // TODO Internationalize

    @PropertyId(Municipality.PROP_NAME)
    private TextField name;

    @Override
    protected Class<Municipality> getEntityType() {
        return Municipality.class;
    }

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        name = new TextField("Name");
        formLayout.addComponent(name);
    }
}
