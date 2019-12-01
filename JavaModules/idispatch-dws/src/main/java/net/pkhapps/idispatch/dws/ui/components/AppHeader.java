package net.pkhapps.idispatch.dws.ui.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jetbrains.annotations.Nullable;

@CssImport("./styles/app-header.css")
public class AppHeader extends Div {

    private Div dispatchCenterName;
    private Div userName;

    public AppHeader() {
        addClassName("app-header");
        var title = new Div(new Text("iDispatch DWS"));
        title.addClassName("title");

        dispatchCenterName = new Div();
        dispatchCenterName.addClassName("dispatch-center-name");

        userName = new Div();
        userName.addClassName("user-name");

        var left = new Div();
        left.addClassName("left");

        var center = new Div();
        center.addClassName("center");

        var right = new Div();
        right.addClassName("right");

        add(left, center, right);

        left.add(title, dispatchCenterName);
        right.add(VaadinIcon.USER.create(), userName);
    }

    public void setDispatchCenterName(@Nullable String dispatchCenterName) {
        this.dispatchCenterName.setText(dispatchCenterName);
    }

    public void setUserName(@Nullable String userName) {
        this.userName.setText(userName);
    }
}
