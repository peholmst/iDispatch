package net.pkhapps.idispatch.dws.ui.layouts;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.RouterLayout;
import net.pkhapps.idispatch.dws.ui.components.AppHeader;
import net.pkhapps.idispatch.dws.ui.viewlets.BulletinBoardViewlet;
import net.pkhapps.idispatch.dws.ui.viewlets.ClockViewlet;

import java.util.Objects;

@Push
@StyleSheet("https://fonts.googleapis.com/css?family=Roboto:100,300,400,500,700&display=swap")
@CssImport("./styles/primary-monitor-layout.css")
public class PrimaryMonitorLayout extends Div implements RouterLayout {

    private final Div routeContainer;

    public PrimaryMonitorLayout() {
        addClassName("primary-monitor-layout");
        setSizeFull();
        var header = new AppHeader();
        header.setDispatchCenterName("Pargas FBK");
        header.setUserName("Joe Cool");

        var clockViewlet = new ClockViewlet();
        var bulletinBoardViewlet = new BulletinBoardViewlet();
        var leftSidePanel = new Div(clockViewlet, bulletinBoardViewlet);
        var rightSidePanel = new Div();
        routeContainer = new Div();

        var innerSplitLayout = new SplitLayout(routeContainer, rightSidePanel);
        var outerSplitLayout = new SplitLayout(leftSidePanel, innerSplitLayout);
        outerSplitLayout.setSizeFull();
        add(header);
        add(outerSplitLayout);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        if (content != null) {
            routeContainer.getElement().appendChild(Objects.requireNonNull(content.getElement()));
        }
    }
}
