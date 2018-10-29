package net.pkhapps.idispatch.workstation.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.pkhapps.idispatch.workstation.ui.component.MapView;
import net.pkhapps.idispatch.workstation.ui.component.ResourceView;

@Route("secondary")
@PageTitle("iDispatch Workstation (Monitor 2)")
@HtmlImport("frontend://styles/secondary-monitor-styles.html")
public class SecondaryMonitorScreen extends Composite<VerticalLayout> {

    private final VerticalLayout content;
    private final SplitLayout splitLayout;
    private final MapView mapView;
    private final ResourceView resourceView;

    public SecondaryMonitorScreen() {
        this.mapView = new MapView();
        this.resourceView = new ResourceView();
        splitLayout = new SplitLayout(mapView, resourceView);
        splitLayout.setOrientation(SplitLayout.Orientation.HORIZONTAL);
        splitLayout.setSizeFull();
        content = new VerticalLayout(createHeader(), splitLayout);
        content.setSpacing(false);
        content.setMargin(false);
        content.setPadding(false);
        content.setSizeFull();
    }

    private Component createHeader() {
        var header = new Div();
        var title = new Span("iDispatch Workstation (Monitor 2)");
        header.add(title);
        return header;
    }

    @Override
    public VerticalLayout getContent() {
        return content;
    }
}
