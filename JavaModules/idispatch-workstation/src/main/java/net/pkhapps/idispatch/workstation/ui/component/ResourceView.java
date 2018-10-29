package net.pkhapps.idispatch.workstation.ui.component;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import net.pkhapps.idispatch.client.v3.ResourceStatus;

public class ResourceView extends Composite<FlexLayout> {

    private final FlexLayout content;

    public ResourceView() {
        var header = new Div(new Span("Resurser"));
        var tabs = new Tabs(new Tab("Tabell"), new Tab("Tavla"));
        var grid = new Grid<ResourceStatus>();

        content = new FlexLayout(header, tabs, grid);
        content.getElement().getStyle().set("flex-direction", "column");

        content.setFlexGrow(1, grid);
    }

    @Override
    protected FlexLayout initContent() {
        return content;
    }
}
