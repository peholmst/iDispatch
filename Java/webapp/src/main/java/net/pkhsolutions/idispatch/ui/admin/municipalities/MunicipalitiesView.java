package net.pkhsolutions.idispatch.ui.admin.municipalities;

import com.vaadin.ui.Table;
import net.pkhsolutions.idispatch.entity.Municipality;
import net.pkhsolutions.idispatch.entity.repository.MunicipalityRepository;
import net.pkhsolutions.idispatch.ui.admin.AbstractCrudView;
import net.pkhsolutions.idispatch.ui.admin.AdminUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

@VaadinView(name = MunicipalitiesView.VIEW_NAME, ui = AdminUI.class)
@UIScope
public class MunicipalitiesView extends AbstractCrudView<Municipality, MunicipalityRepository> {

    // TODO Internationalize

    public static final String VIEW_NAME = "municipalities";

    @Autowired
    MunicipalityRepository repository;

    @Override
    protected String getTitle() {
        return "Municipalities";
    }

    @Override
    protected MunicipalityRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<Municipality> getEntityClass() {
        return Municipality.class;
    }

    @Override
    protected void openCreateWindow(SaveCallback<Municipality> saveCallback) {
        openWindow("Add Municipality", createForm(MunicipalityForm.class, new Municipality(), saveCallback));
    }

    @Override
    protected void openEditWindow(Municipality entity, SaveCallback<Municipality> saveCallback) {
        openWindow("Edit Municipality", createForm(MunicipalityForm.class, entity, saveCallback));
    }

    @Override
    protected void configureTable(Table table) {
        table.setVisibleColumns(Municipality.PROP_NAME);
    }
}
