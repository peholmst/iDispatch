package net.pkhsolutions.idispatch.ui.admin.assignmenttypes;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import net.pkhsolutions.idispatch.entity.AssignmentType;
import net.pkhsolutions.idispatch.ui.admin.AbstractForm;
import org.springframework.context.annotation.Scope;
import org.vaadin.spring.VaadinComponent;

@VaadinComponent
@Scope("prototype")
class AssignmentTypeForm extends AbstractForm<AssignmentType> {

    // TODO Internationalize

    @PropertyId(AssignmentType.PROP_CODE)
    private TextField code;

    @PropertyId(AssignmentType.PROP_DESCRIPTION)
    private TextField description;

    @Override
    protected Class<AssignmentType> getEntityType() {
        return AssignmentType.class;
    }

    @Override
    protected void createAndAddFields(FormLayout formLayout) {
        code = new TextField("Code");
        formLayout.addComponent(code);

        description = new TextField("Description");
        formLayout.addComponent(description);
    }
}
