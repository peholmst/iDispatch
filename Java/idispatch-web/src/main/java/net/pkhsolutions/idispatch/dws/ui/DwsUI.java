package net.pkhsolutions.idispatch.dws.ui;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Locale;
import com.github.peholmst.i18n4vaadin.annotations.Message;
import com.github.peholmst.i18n4vaadin.cdi.annotations.I18nSupportedLocales;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.access.AccessControl;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import javax.inject.Inject;
import net.pkhsolutions.idispatch.ejb.common.Roles;

/**
 *
 * @author peholmst
 */
@CDIUI
@Theme("dws")
@Widgetset("net.pkhsolutions.idispatch.dws.ui.DwsWidgetset")
public class DwsUI extends UI {

    @Inject
    private CDIViewProvider cdiViewProvider;
    @Inject
    private OpenTicketsViewlet openTicketsViewlet;
    @Inject
    private MenuViewlet menuViewlet;
    @I18nSupportedLocales({@Locale(language = "sv"), @Locale(language = "fi")})
    @Inject
    private I18N i18n;
    private Refresher refresher;
    @Inject
    private DwsUIBundle bundle;
    @Inject
    private AccessControl accessControl;

    @Message(key = "loggedInAs", value = "Inloggad som {0}")
    @Override
    protected void init(VaadinRequest request) {
        refresher = new Refresher();
        refresher.setRefreshInterval(5000);
        addExtension(refresher);

        getPage().setTitle("iDispatch DWS");

        VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        HorizontalLayout header = new HorizontalLayout();
        header.setWidth("100%");
        header.addStyleName("header");
        {
            Label lbl = new Label("iDispatch DWS");
            lbl.addStyleName("title");
            header.addComponent(lbl);
        }
        {
            Label lbl = new Label(bundle.loggedInAs(accessControl.getPrincipalName()));
            lbl.addStyleName("current-user");
            header.addComponent(lbl);
            lbl.setSizeUndefined();
            header.setComponentAlignment(lbl, Alignment.MIDDLE_RIGHT);
        }
        root.addComponent(header);

        HorizontalSplitPanel splitLayout = new HorizontalSplitPanel();
        splitLayout.setSizeFull();
        splitLayout.setSplitPosition(200, Unit.PIXELS);
        root.addComponent(splitLayout);
        root.setExpandRatio(splitLayout, 1);

        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSizeFull();
        sidebar.addComponent(menuViewlet);
        if (accessControl.isUserInRole(Roles.DISPATCHER)) {
            sidebar.addComponent(openTicketsViewlet);
            sidebar.setExpandRatio(openTicketsViewlet, 1);
            refresher.addListener(openTicketsViewlet);
        }

        splitLayout.setFirstComponent(sidebar);

        Panel viewContainer = new Panel();
        viewContainer.addStyleName(Reindeer.PANEL_LIGHT);
        viewContainer.setSizeFull();

        splitLayout.setSecondComponent(viewContainer);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(cdiViewProvider);
        navigator.setErrorView(new ErrorView());
    }
}
