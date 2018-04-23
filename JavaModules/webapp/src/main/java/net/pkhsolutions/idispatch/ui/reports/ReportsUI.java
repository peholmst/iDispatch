package net.pkhsolutions.idispatch.ui.reports;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import net.pkhsolutions.idispatch.ui.common.ErrorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

@VaadinUI(path = "/reports")
@Theme(ReportsTheme.THEME_NAME)
public class ReportsUI extends UI {

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final Navigator navigator = new Navigator(this, this);
        navigator.setErrorView(ErrorView.class);
        navigator.addProvider(viewProvider);
    }
}
