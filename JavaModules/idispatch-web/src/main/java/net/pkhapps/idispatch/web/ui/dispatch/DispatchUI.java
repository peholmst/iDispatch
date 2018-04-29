package net.pkhapps.idispatch.web.ui.dispatch;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.*;
import net.pkhapps.idispatch.web.ui.dispatch.view.ErrorView;
import net.pkhapps.idispatch.web.ui.dispatch.viewlet.OpenAssignmentsViewlet;
import net.pkhapps.idispatch.web.ui.dispatch.viewlet.ResourcesViewlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

/**
 * TODO Document me
 */
@SpringUI(path = "/dispatch")
@Theme(DispatchTheme.THEME_NAME)
public class DispatchUI extends UI {

    @Autowired
    private ResourcesViewlet resourcesViewlet;

    @Autowired
    private OpenAssignmentsViewlet openAssignmentsViewlet;

    @Autowired
    private SpringNavigator navigator;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(buildLayout());
    }

    @NonNull
    private Component buildLayout() {
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

        return root;
    }

    @NonNull
    private Component buildHeader() {
        HorizontalLayout root = new HorizontalLayout();
        root.setWidth(100, Unit.PERCENTAGE);
        root.addStyleName(DispatchTheme.HEADER);
        root.setMargin(true);
        root.setSpacing(true);
        Label title = new Label("Dispatcher");
        title.addStyleName("title");
        root.addComponent(title);

        Button logout = new Button(VaadinIcons.POWER_OFF);
        logout.setDescription("Logga ut");
        root.addComponent(logout);
        root.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
        return root;
    }
}
