package net.pkhapps.idispatch.dispatcher.console.i18n;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * TODO Document me!
 */
public class BundleTranslator implements Translator {

    private final ResourceBundle bundle;

    public BundleTranslator(@NotNull Locale locale) {
        bundle = ResourceBundle.getBundle("bundles/messages", locale);
    }

    @Override
    public @NotNull Locale getLocale() {
        return bundle.getLocale();
    }

    @Override
    public @NotNull String get(@NotNull String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }

    @Override
    public @NotNull String get(@NotNull String key, @NotNull Object... params) {
        try {
            return MessageFormat.format(bundle.getString(key), params);
        } catch (MissingResourceException ex) {
            return "!" + key;
        }
    }
}
