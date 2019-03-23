package net.pkhapps.idispatch.dispatcher.console.i18n;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO Document me!
 */
public class I18N {

    public static final Locale SWEDISH = new Locale("sv", "FI");
    public static final Locale FINNISH = new Locale("fi", "FI");

    private static final I18N INSTANCE = new I18N();

    private final SimpleObjectProperty<Locale> locale = new SimpleObjectProperty<>(SWEDISH);
    private final ObservableObjectValue<Translator> translator;
    private final Map<String, ObservableStringValue> translations = new ConcurrentHashMap<>();

    I18N() {
        translator = Bindings.createObjectBinding(() -> new BundleTranslator(locale.get()), locale);
    }

    public static @NotNull I18N getInstance() {
        return INSTANCE;
    }

    public @NotNull Locale getLocale() {
        return locale.get();
    }

    public void setLocale(@NotNull Locale locale) {
        Objects.requireNonNull(locale, "locale must not be null");
        this.locale.set(locale);
    }

    public @NotNull Property<Locale> localeProperty() {
        return locale;
    }

    public @NotNull Translator getTranslator() {
        return translator.get();
    }

    public @NotNull ObservableObjectValue<Translator> translatorProperty() {
        return translator;
    }

    public @NotNull ObservableStringValue translate(@NotNull Translatable key) {
        return translate(key.getKey());
    }

    public @NotNull ObservableStringValue translate(@NotNull String key) {
        var translation = translations.get(key);
        if (translation == null) {
            translation = Bindings.createStringBinding(() -> getTranslator().get(key), translatorProperty());
            translations.put(key, translation);
        }
        return translation;
    }
}
