package net.pkhapps.idispatch.dispatcher.console.main;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import net.pkhapps.idispatch.dispatcher.console.context.ApplicationContext;
import net.pkhapps.idispatch.dispatcher.console.i18n.I18N;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import static net.pkhapps.idispatch.dispatcher.console.component.ComponentFactory.hBoxSpacer;

/**
 * TODO Document me!
 */
public class StatusPane extends HBox {

    public StatusPane(@NotNull ApplicationContext applicationContext) {
        setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
        setPadding(new Insets(3, 10, 3, 10));
        setSpacing(10);

        var user = new Label(String.format("%s (%s)", applicationContext.getUser().getFullName(),
                applicationContext.getUser().getOrganization()), new FontIcon(FontAwesomeSolid.USER));
        user.setTooltip(new Tooltip(I18N.getInstance().getTranslator().get("main.StatusPane.user")));

        var server = new Label(applicationContext.getServer().getName(), new FontIcon(FontAwesomeSolid.SERVER));
        server.setTooltip(new Tooltip(I18N.getInstance().getTranslator().get("main.StatusPane.server")));

        var statusIndicator = new SystemStatusIndicator(applicationContext.getSystemStatusSummary());

        getChildren().addAll(user, hBoxSpacer(), server, statusIndicator);
    }
}
