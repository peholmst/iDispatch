package net.pkhapps.idispatch.web.ui.common;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class ViewletTitle extends HorizontalLayout {

    public ViewletTitle(String text) {
        setWidth(100, Unit.PERCENTAGE);
        addStyleName("title");
        addComponentsAndExpand(new Label(text));
    }
}
