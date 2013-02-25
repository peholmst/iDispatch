package net.pkhsolutions.idispatch.dws.ui.masterdata;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.annotations.Messages;
import com.vaadin.cdi.VaadinView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.dws.ui.MenuViewlet;
import net.pkhsolutions.idispatch.ejb.masterdata.Backend;
import net.pkhsolutions.idispatch.ejb.masterdata.MunicipalityEJB;
import net.pkhsolutions.idispatch.entity.Municipality;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author peholmst
 */
@VaadinView(MunicipalityView.VIEW_ID)
public class MunicipalityView extends AbstractMasterDataView<Municipality> {

    public static final String VIEW_ID = "municipalityMasterData";
    @Inject
    private MunicipalityEJB municipality;
    @Inject
    private MunicipalityViewBundle bundle;
    @Inject
    private I18N i18n;

    @Message(key = "title", value = "Kommuner")
    @Override
    protected String getTitle() {
        return bundle.title();
    }

    @Override
    protected Backend<Municipality> getBackend() {
        return municipality;
    }

    @Messages({
        @Message(key = "nameFi", value = "Namn (finska)"),
        @Message(key = "nameSv", value = "Namn (svenska)")
    })
    @Override
    protected void setUpFormFields(FormLayout formLayout, FieldGroup fieldGroup) {
        TextField nameFi = new TextField(bundle.nameFi());
        nameFi.setWidth("200px");
        nameFi.setNullRepresentation("");
        fieldGroup.bind(nameFi, "nameFi");
        formLayout.addComponent(nameFi);

        TextField nameSv = new TextField(bundle.nameSv());
        nameSv.setWidth("200px");
        nameSv.setNullRepresentation("");
        fieldGroup.bind(nameSv, "nameSv");
        formLayout.addComponent(nameSv);
    }

    @Override
    protected void setUpTable(Table table) {
        table.setVisibleColumns(new String[]{"nameFi", "nameSv"});
        table.setColumnHeaders(new String[]{bundle.nameFi(), bundle.nameSv()});
        table.setSortContainerPropertyId("name" + StringUtils.capitalize(i18n.getLocale().getLanguage()));
    }

    @Override
    protected Class<Municipality> getEntityClass() {
        return Municipality.class;
    }

    @Override
    protected Municipality createNewEntity() {
        return new Municipality.Builder().build();
    }

    @Override
    protected Municipality createCopyOfEntity(Municipality entity) {
        return new Municipality.Builder(entity).build();
    }

    public static class MenuItemRegistrar {

        @Inject
        MunicipalityViewBundle bundle;

        public void register(@Observes MenuViewlet.MenuItemRegistrationEvent event) {
            event.getMenu().addMenuItem(bundle.title(), VIEW_ID);
        }
    }
}
