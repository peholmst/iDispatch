package net.pkhapps.idispatch.dws.ui.viewlets;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

@CssImport("./styles/bulletin-board-viewlet.css")
public class BulletinBoardViewlet extends Div {

    public BulletinBoardViewlet() {
        addClassName("bulletin-board-viewlet");

        var toolbar = new Div();
        toolbar.addClassName("toolbar");

        var add = new Button(VaadinIcon.PLUS_CIRCLE.create());
        toolbar.add(new Div(new Text("Anslagstavla")), add);

        var scrollable = new Div();
        scrollable.addClassName("scrollable");

        add(toolbar, scrollable);
    }
}
