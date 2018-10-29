package net.pkhapps.idispatch.workstation.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;

import javax.annotation.Nonnull;

@Route("")
public class StartScreen extends Div {

    public StartScreen() {
        // TODO Translate and make UI look better
        var openPrimaryMonitorScreen = new Button("Öppna monitor 1", evt -> openPrimaryMonitorScreen());
        var openSecondaryMonitorScreen = new Button("Öppna monitor 2", evt -> openSecondaryMonitorScreen());
        add(openPrimaryMonitorScreen, openSecondaryMonitorScreen);
    }

    private void openPrimaryMonitorScreen() {
        openBrowserWindow(PrimaryMonitorScreen.class);
    }

    private void openSecondaryMonitorScreen() {
        openBrowserWindow(SecondaryMonitorScreen.class);
    }

    private void openBrowserWindow(@Nonnull Class<? extends Component> navigationTarget) {
        getUI().map(UI::getRouter).map(r -> r.getUrl(navigationTarget)).ifPresent(url -> openBrowserWindow(url,
                navigationTarget.getSimpleName()));
    }

    private void openBrowserWindow(@Nonnull String url, @Nonnull String windowName) {
        getUI().map(UI::getPage).ifPresent(page -> page.executeJavaScript("window.open($0, $1, 'titlebar=no')",
                url, windowName));
    }
}
