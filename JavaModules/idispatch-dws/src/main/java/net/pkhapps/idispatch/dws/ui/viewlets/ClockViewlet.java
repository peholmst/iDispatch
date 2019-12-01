package net.pkhapps.idispatch.dws.ui.viewlets;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@CssImport("./styles/clock-viewlet.css")
public class ClockViewlet extends Div {

    private final Div time;
    private final Div date;

    public ClockViewlet() {
        addClassName("clock-viewlet");
        time = new Div();
        time.addClassName("time");
        date = new Div();
        date.addClassName("date");

        add(time, date);

        // TODO Remove this dummy data
        time.setText("13:44:23");
        date.setText("söndag 1 december 2019");
    }
}
