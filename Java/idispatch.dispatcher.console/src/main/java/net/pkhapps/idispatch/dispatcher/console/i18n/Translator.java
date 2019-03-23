package net.pkhapps.idispatch.dispatcher.console.i18n;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * TODO Document me!
 */
public interface Translator {

    @NotNull Locale getLocale();

    default @NotNull String get(@NotNull Translatable translatable) {
        return get(translatable.getKey());
    }

    default @NotNull String get(@NotNull Translatable translatable, @NotNull Object... params) {
        return get(translatable.getKey(), params);
    }

    @NotNull String get(@NotNull String key);

    @NotNull String get(@NotNull String key, @NotNull Object... params);
}
