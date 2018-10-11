package net.pkhapps.idispatch.web.ui.dispatch;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.dispatch.view.ErrorView;
import net.pkhapps.idispatch.web.ui.dispatch.view.ViewWithTitle;
import net.pkhapps.idispatch.web.ui.dispatch.viewlet.OpenAssignmentsViewlet;
import net.pkhapps.idispatch.web.ui.dispatch.viewlet.ResourcesViewlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * TODO Document me
 */
@SpringUI(path = "/dispatch")
@Theme(DispatchTheme.THEME_NAME)
public class DispatchUI extends UI {

    @Autowired
    private I18N i18n;

    @Autowired
    private ResourcesViewlet resourcesViewlet;

    @Autowired
    private OpenAssignmentsViewlet openAssignmentsViewlet;

    @Autowired
    private SpringNavigator navigator;
    private Label appTitle;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(buildLayout());
    }

    @NonNull
    private Component buildLayout() {
        setLocale(i18n.getLocale());

        VerticalLayout root = new VerticalLayout();
        root.setMargin(false);
        root.setSpacing(false);
        root.setSizeFull();

        root.addComponent(buildHeader());

        HorizontalSplitPanel hSplitPanel = new HorizontalSplitPanel();
        root.addComponentsAndExpand(hSplitPanel);

        VerticalSplitPanel vSplitPanel = new VerticalSplitPanel();
        vSplitPanel.setSizeFull();

        hSplitPanel.setSecondComponent(vSplitPanel);
        hSplitPanel.setSplitPosition(500, Unit.PIXELS, true);

        vSplitPanel.setFirstComponent(resourcesViewlet);
        vSplitPanel.setSecondComponent(openAssignmentsViewlet);

        navigator.init(this, (ViewDisplay) view -> hSplitPanel.setFirstComponent((Component) view));
        navigator.setErrorView(ErrorView.class);
        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                if (event.getNewView() instanceof ViewWithTitle) {
                    setAppTitle(((ViewWithTitle) event.getNewView()).getTitle());
                } else {
                    setAppTitle(null);
                }
            }
        });

        return root;
    }

    private void setAppTitle(@Nullable String title) {
        final String appName = i18n.get("appName");
        if (title == null) {
            getPage().setTitle(appName);
            appTitle.setValue(appName);
        } else {
            getPage().setTitle(String.format("%s - %s", appName, title));
            appTitle.setValue(title);
        }
    }

    @NonNull
    private Component buildHeader() {
        HorizontalLayout root = new HorizontalLayout();
        root.setWidth(100, Unit.PERCENTAGE);
        root.addStyleName(DispatchTheme.HEADER);
        root.setMargin(true);
        root.setSpacing(true);
        appTitle = new Label();
        appTitle.addStyleName("title");
        root.addComponent(appTitle);

        Button logout = new Button(VaadinIcons.POWER_OFF);
        logout.setDescription("Logga ut"); // TODO translate
        root.addComponent(logout);
        root.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
        return root;
    }
}
