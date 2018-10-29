package net.pkhapps.idispatch.workstation.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("primary")
@PageTitle("iDispatch Workstation (Monitor 1)")
public class PrimaryMonitorScreen extends Div {

    PrimaryMonitorScreen() {
        add(new Label("Primary"));
    }
}
