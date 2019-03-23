package net.pkhapps.idispatch.dispatcher.console.component;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import net.pkhapps.idispatch.dispatcher.console.i18n.I18N;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * TODO Document me!
 */
public final class ComponentFactory {

    private ComponentFactory() {
    }

    public static @NotNull Label translatedLabel(@NotNull String key) {
        var lbl = new Label();
        lbl.textProperty().bind(I18N.getInstance().translate(key));
        lbl.setMinWidth(Region.USE_PREF_SIZE);
        return lbl;
    }

    public static @NotNull Button translatedButton(@NotNull String key) {
        var btn = new Button();
        btn.textProperty().bind(I18N.getInstance().translate(key));
        return btn;
    }

    public static @NotNull <V> ComboBox<V> comboBox(Function<V, String> itemCaptionProvider) {
        var comboBox = new ComboBox<V>();
        comboBox.setCellFactory(param -> new ListCellWithCaptionProvider<>(itemCaptionProvider));
        comboBox.setButtonCell(new ListCellWithCaptionProvider<>(itemCaptionProvider));
        return comboBox;
    }

    private static class ListCellWithCaptionProvider<V> extends ListCell<V> {

        private final Function<V, String> captionProvider;

        private ListCellWithCaptionProvider(@NotNull Function<V, String> captionProvider) {
            this.captionProvider = captionProvider;
        }

        @Override
        protected void updateItem(V item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText(captionProvider.apply(item));
            }
        }
    }
}
