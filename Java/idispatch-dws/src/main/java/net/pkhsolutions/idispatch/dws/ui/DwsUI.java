package net.pkhsolutions.idispatch.dws.ui;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.annotations.Locale;
import com.github.peholmst.i18n4vaadin.cdi.annotations.I18nSupportedLocales;
import com.github.wolfie.refresher.Refresher;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.cdi.Root;
import com.vaadin.cdi.VaadinUI;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

import javax.inject.Inject;

/**
 *
 * @author peholmst
 */
@VaadinUI
@Root
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

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("iDispatch DWS");

        HorizontalSplitPanel rootLayout = new HorizontalSplitPanel();
        rootLayout.setSizeFull();
        rootLayout.setSplitPosition(200, Unit.PIXELS);
        setContent(rootLayout);

        VerticalLayout sidebar = new VerticalLayout();
        sidebar.setSizeFull();
        sidebar.addComponent(menuViewlet);
        sidebar.addComponent(openTicketsViewlet);
        sidebar.setExpandRatio(openTicketsViewlet, 1);

        rootLayout.setFirstComponent(sidebar);

        Panel viewContainer = new Panel();
        viewContainer.addStyleName(Reindeer.PANEL_LIGHT);
        viewContainer.setSizeFull();

        rootLayout.setSecondComponent(viewContainer);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(cdiViewProvider);
        navigator.setErrorView(new ErrorView());

        refresher = new Refresher();
        refresher.setRefreshInterval(5000);
        refresher.addListener(openTicketsViewlet);
        addExtension(refresher);
    }
}
