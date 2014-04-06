package net.pkhsolutions.idispatch.dws.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ErrorView extends VerticalLayout implements View {

    private Label detailMessage;

    public ErrorView() {
        setSpacing(true);
        setMargin(true);

        Label header = new Label("The view does not exist");
        header.addStyleName(Reindeer.LABEL_H1);
        addComponent(header);

        detailMessage = new Label();
        addComponent(detailMessage);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        detailMessage.setValue(String.format("You attempted to navigate to the view \"%s\". Unfortunately, this view does not exist.", viewChangeEvent.getViewName()));
    }
}
