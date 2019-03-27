package net.pkhapps.idispatch.dispatcher.console.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import net.pkhapps.idispatch.dispatcher.console.i18n.I18N;
import net.pkhapps.idispatch.dispatcher.console.io.SystemStatusSummary;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import static java.util.Objects.requireNonNull;

/**
 * TODO Document me!
 */
public class SystemStatusIndicator extends Label {

    private final FontIcon icon;
    private final ChangeListener<SystemStatusSummary.State> stateChangeListener = (observable, oldValue, newValue) -> setState(newValue);

    public SystemStatusIndicator(@NotNull SystemStatusSummary systemStatusSummary) {
        icon = new FontIcon();
        setGraphic(icon);
        requireNonNull(systemStatusSummary);
        setState(systemStatusSummary.state().get());
        systemStatusSummary.state().addListener(new WeakChangeListener<>(stateChangeListener));
    }

    private void setState(@NotNull SystemStatusSummary.State state) {
        switch (state) {
            case ALL_SYSTEMS_OPERATIONAL:
                icon.setIconCode(FontAwesomeSolid.CHECK_CIRCLE);
                icon.setIconColor(Color.DARKGREEN);
                break;
            case SOME_SYSTEMS_OPERATIONAL:
                icon.setIconCode(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
                icon.setIconColor(Color.ORANGE);
                break;
            case NO_SYSTEMS_OPERATIONAL:
                icon.setIconCode(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
                icon.setIconColor(Color.RED);
                break;
            default:
                icon.setIconCode(FontAwesomeSolid.QUESTION_CIRCLE);
                icon.setIconColor(Color.GRAY);
                break;
        }
        setTooltip(new Tooltip(I18N.getInstance().getTranslator().get("main.SystemStatusIndicator." + state.name())));
    }
}
